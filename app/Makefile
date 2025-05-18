.PHONY: build run test clean all

# Download JavaFX SDK if needed
javafx-sdk:
    curl -L "https://download2.gluonhq.com/openjfx/17.0.1/openjfx-17.0.1_osx-x64_bin-sdk.zip" -o javafx-sdk.zip
    unzip -o javafx-sdk.zip
    rm javafx-sdk.zip

# Build with JavaFX
build: javafx-sdk
    export PATH_TO_FX="$(PWD)/javafx-sdk-17.0.1/lib" && \
    mvn clean package

# Run tests with JavaFX
test: javafx-sdk
    export PATH_TO_FX="$(PWD)/javafx-sdk-17.0.1/lib" && \
    mvn test -Dtestfx.robot=glass -Dglass.platform=Monocle -Dmonocle.platform=Headless -Dprism.order=sw

# Run the application
run: javafx-sdk
    export PATH_TO_FX="$(PWD)/javafx-sdk-17.0.1/lib" && \
    java --module-path "$$PATH_TO_FX" --add-modules javafx.controls,javafx.fxml -jar target/courier-app-1.0-SNAPSHOT.jar

# Clean the build artifacts
clean:
    mvn clean

# Build and run
all: build run