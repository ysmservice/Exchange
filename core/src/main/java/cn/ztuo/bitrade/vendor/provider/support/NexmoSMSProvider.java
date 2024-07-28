package cn.ztuo.bitrade.vendor.provider.support;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import cn.ztuo.bitrade.dto.SmsDTO;
import cn.ztuo.bitrade.service.SmsService;
import cn.ztuo.bitrade.util.MessageResult;
import cn.ztuo.bitrade.vendor.provider.SMSProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class NexmoSMSProvider implements SMSProvider {

    private String apiKey;
    private String apiSecret;
    private String from;

    @Autowired
    private SmsService smsService;

    public NexmoSMSProvider(String apiKey, String apiSecret, String from) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.from = from;
    }

    public static String getName() {
        return "nexmo";
    }

    @Override
    public MessageResult sendSingleMessage(String mobile, String content) throws Exception {
        log.info("Sending SMS to {}: {}", mobile, content);

        VonageClient client = VonageClient.builder()
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .build();

        TextMessage message = new TextMessage(from, mobile, content);
        SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

        log.info("Response: {}", response);

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            return new MessageResult(0, "SMS sent successfully");
        } else {
            return new MessageResult(500, "Failed to send SMS: " + response.getMessages().get(0).getErrorText());
        }
    }

    @Override
    public MessageResult sendMessageByTempId(String mobile, String content, String templateId) throws Exception {
        // ここでテンプレートIDを使用したSMS送信を実装します。
        return sendSingleMessage(mobile, content);
    }
}
