package dygitan.demo.aws.vpc;

public class App {

    public static void main(final String[] args) {
        final software.amazon.awscdk.core.App app = new software.amazon.awscdk.core.App();
        new AppStack(app, "VPCDemo", "cdk-demo-vpc");
        app.synth();
    }
}
