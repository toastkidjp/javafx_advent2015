package jp.toastkid.gui.jfx.script;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * JavaFX による 学習用アプリケーション.
 * @author Toast kid
 * @version 0.0.1
 */
public final class Main extends Application {
    /** fxml ファイル. */
    private static final String FXML_PATH = "res/scenes/Main.fxml";

    /** コントローラ. */
    private MainController controller;

    @Override
    public void start(final Stage stage) {
        final long start = System.currentTimeMillis();
        stage.setTitle("ScriptEngine Verification");
        final Scene scene = readScene(stage);
        stage.setScene(scene);
        stage.setWidth(800);
        stage.setHeight(600);
        stage.initStyle(StageStyle.DECORATED);
        stage.setOnCloseRequest(
            (event) -> {
            if (event.getEventType().equals(WindowEvent.WINDOW_CLOSE_REQUEST)) {
                controller.closeApplication(null);
            }
        });
        stage.show();
        controller.setStatus("完了 - " + (System.currentTimeMillis() - start) + "[ms]");
    }

    /**
     * コントローラに stage を渡しシーンファイルを読込.
     * @param stage Stage オブジェクト
     * @return Scene オブジェクト
     */
    private final Scene readScene(final Stage stage) {
        try {
            final FXMLLoader loader = new FXMLLoader(new File(FXML_PATH).toURI().toURL());
            final VBox loaded = (VBox) loader.load();
            controller = (MainController) loader.getController();
            controller.setThisStage(stage);
            return new Scene(loaded);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        controller = null;
    }
    /**
     *
     * @param args
     */
    public static void main(final String[] args) {
        Application.launch(Main.class);
    }
}
