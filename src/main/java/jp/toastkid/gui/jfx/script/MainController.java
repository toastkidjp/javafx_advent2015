package jp.toastkid.gui.jfx.script;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.reactfx.Subscription;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import jp.toastkid.highlight.GroovyHighlight;
import jp.toastkid.libs.ScriptRunner;
import jp.toastkid.models.Language;

/**
 * JavaFX のコントローラ.
 * @author Toast kid
 *
 */
public final class MainController implements Initializable {
    /** run script keyboard shortcut. */
    private static final KeyCodeCombination RUN_SCRIPT
        = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.CONTROL_DOWN);
    /** 画面下部のstatusラベル. */
    @FXML
    public Label status;
    /** in script area. */
    @FXML
    public HBox scripterArea;
    /** script input. */
    @FXML
    public CodeArea scripterInput;
    /** script output. */
    @FXML
    public CodeArea scripterOutput;

    /** names of script language. */
    @FXML
    public ComboBox<String> scriptLanguage;
    /** Stylesheet selector. */
    @FXML
    public ComboBox<String> style;
    @FXML
    public TextField scriptName;

    private Subscription subscription;

    /** 関数群. */
    private ScriptRunner func;
    /** Stage. */
    private Stage thisStage;

    @Override
    public final void initialize(
            final URL url,
            final ResourceBundle resourcebundle
            ) {
        scriptLanguage.getSelectionModel().select(0);
        Platform.runLater( () -> {
            readStyleSheets();
            setStylesheet();
            }
        );

        scripterInput.setParagraphGraphicFactory(LineNumberFactory.get(scripterInput));
        scripterOutput.setParagraphGraphicFactory(LineNumberFactory.get(scripterOutput));
        scripterInput.setOnKeyPressed((e) -> {
            if (RUN_SCRIPT.match(e)) {
                runScript();
            }
        });
        subscription = new GroovyHighlight(scripterInput).highlight();
    }

    /**
     * run script use input plain text.
     */
    @FXML
    private void runScript() {
        final Language lang = Language.valueOf(
                scriptLanguage.getSelectionModel().getSelectedItem().toUpperCase());
        final String result = ScriptRunner.find(lang).run(scripterInput.getText()).get();
        if (StringUtils.isEmpty(result)) {
            return;
        }
        scripterOutput.replaceText(result);
    }

    /**
     * read stylesheets.
     */
    private void readStyleSheets() {
        final File cssDir = new File(Style.USER_DEFINED_PATH);
        if (!cssDir.exists()) {
            return;
        }
        final ObservableList<String> items = style.getItems();
        final String[] styles = cssDir.list();
        for (final String style : styles) {
            if (!style.contains(".")) {
                continue;
            }
            final String upperCase = style.toUpperCase().substring(0, style.indexOf("."));
            if (items.contains(upperCase)) {
                continue;
            }
            items.add(upperCase);
        }
    }
    /**
     * set stylesheet name in combobox.
     */
    private void setStylesheet() {
        @SuppressWarnings("rawtypes")
        final SingleSelectionModel selectionModel = style.getSelectionModel();
        selectionModel.select(0);
    }

    /**
     * set stylesheet.
     */
    @FXML
    public void callSetOnStyle() {
        final String styleName = style.getItems().get(style.getSelectionModel().getSelectedIndex())
                                    .toString();
        if (StringUtils.isEmpty(styleName)) {
            return;
        }
        final ObservableList<String> stylesheets = thisStage.getScene().getStylesheets();
        if (stylesheets != null) {
            stylesheets.clear();
        }
        if ("MODENA".equals(styleName) || "CASPIAN".equals(styleName)) {
            Application.setUserAgentStylesheet(styleName);
        } else {
            Application.setUserAgentStylesheet("MODENA");
            stylesheets.add(Style.getPath(styleName));
        }
        stylesheets.add(getClass().getClassLoader().getResource("keywords.css").toExternalForm());
    }

    /**
     * アプリケーションを終了する.
     * @param event ActionEvent
     */
    @FXML
    public final void closeApplication(final ActionEvent event) {
        System.exit(0);
    }

    /**
     * stage を set.
     * @param stage
     */
    public final void setThisStage(final Stage stage) {
        thisStage = stage;
    }

    /**
     * set status text.
     * @param str status text
     */
    public final void setStatus(final String str) {
        status.setText(str);
    }
}
