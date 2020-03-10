package camel2.org.apache.camel.example.cdi.aws.s3;

import java.io.File;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import org.apache.camel.LoggingLevel;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.processor.idempotent.FileIdempotentRepository;

public class Application {

    @ContextName("camel-example-aws-s3-cdi")
    static class AwsS3Route {

        public void configure() {
            from("aws-s3://bucket-name?amazonS3Client=#amazonS3Client&deleteAfterRead=false&maxMessagesPerPoll=25&delay=5000")
                    .log(LoggingLevel.INFO, "consuming", "Consumer Fired!")
                    .idempotentConsumer(header("CamelAwsS3ETag"),
                            FileIdempotentRepository.fileIdempotentRepository(new File("target/file.data"), 250, 512000))
                    .log(LoggingLevel.INFO, "Replay Message Sent to file:s3out ${in.header.CamelAwsS3Key}")
                    .to("file:target/s3out?fileName=${in.header.CamelAwsS3Key}");
        }

        @Produces
        @Named("amazonS3Client")
        AmazonS3 amazonS3Client() {
            AWSCredentials credentials = new BasicAWSCredentials("XXXXX", "XXXXX");
            AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);
            AmazonS3ClientBuilder clientBuilder = AmazonS3ClientBuilder.standard().withRegion(Regions.US_WEST_1).withCredentials(credentialsProvider);
            return clientBuilder.build();
        }
    }
}
