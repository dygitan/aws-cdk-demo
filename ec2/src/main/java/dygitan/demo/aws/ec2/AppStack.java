package dygitan.demo.aws.ec2;

import dygitan.demo.aws.shared.SharedStack;
import software.amazon.awscdk.core.CfnTag;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Fn;
import software.amazon.awscdk.services.ec2.*;

import java.util.Arrays;

public class AppStack extends SharedStack {

    public AppStack(final Construct scope, final String id, final String name) {
        super(scope, id, name);

        UserData userData = UserData.forLinux(LinuxUserDataOptions.builder()
            .shebang("!/bin/bash")
            .build());
        userData.addCommands(
            "sudo apt-get update",
            "sudo apt-get install nginx -y",
            "sudo nginx"
        );

        CfnInstance.Builder.create(this, format("Instance"))
            .availabilityZone("ap-southeast-2a")
            .imageId("ami-0a1a4d97d4af3009b") // Ubuntu, 20.04 LTS
            .instanceType(InstanceType.of(InstanceClass.BURSTABLE2, InstanceSize.MICRO).toString())
            .keyName("ec2-demo-instance")
            .subnetId(getCfnSubnet().getRef())
            .securityGroupIds(Arrays.asList(getCfnSecurityGroup().getRef()))
            .tags(Arrays.asList(CfnTag.builder()
                .key("Name")
                .value("demo-instance")
                .build()))
            .userData(Fn.base64(userData.render()))
            .build();
    }
}
