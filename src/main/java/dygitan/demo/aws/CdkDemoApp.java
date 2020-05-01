package dygitan.demo.aws;

import software.amazon.awscdk.core.App;

public class CdkDemoApp {

    public static void main(final String[] args) {
        final App app = new App();
        new CdkDemoStack(app, "CdkDemo");
        app.synth();
    }
}
