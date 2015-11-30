package jp.toastkid.models;

import static org.junit.Assert.*;
import jp.toastkid.models.Language;

import org.junit.Test;

/**
 * Language's test.
 * @author Toast kid
 *
 */
public class LanguageTest {

    /**
     * すべて大文字の時だけオブジェクトを返す.
     */
    @Test
    public final void testValueOf() {
        assertEquals(Language.GROOVY, Language.valueOf("GROOVY"));
        assertEquals(Language.JAVASCRIPT, Language.valueOf("JAVASCRIPT"));
        assertEquals(Language.PYTHON, Language.valueOf("PYTHON"));
    }

    /**
     * 小文字は不可.
     */
    @Test(expected=IllegalArgumentException.class)
    public final void notDefined() {
        Language.valueOf("python");
    }
}
