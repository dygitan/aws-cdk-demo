package dygitan.demo.aws;

import software.amazon.awscdk.core.*;
import software.amazon.awscdk.services.ec2.CfnVPC;

import java.util.Arrays;

public class CdkDemoStack extends Stack {

    public CdkDemoStack(final Construct scope, final String id) {
        this(scope, id, StackProps.builder()
            .env(Environment.builder()
                .region("ap-southeast-2")
                .build())
            .build());
    }

    public CdkDemoStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        CfnVPC cfnVpc = CfnVPC.Builder.create(this, "DemoVpc")
            .cidrBlock("192.168.0.0/22")
            .instanceTenancy("default")
            .tags(Arrays.asList(CfnTag.builder()
                .key("Name")
                .value("vpc-demo")
                .build()))
            .build();
    }
}
