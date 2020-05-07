package dygitan.demo.aws.ec2;

public class App {

    public static void main(final String[] args) {
        final software.amazon.awscdk.core.App app = new software.amazon.awscdk.core.App();
        new AppStack(app, "CdkDemoEC2", "cdk-demo-ec2");
        app.synth();
    }
}
