package integration.pageobjects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;

import integration.AbstractWebTesterIntegrationTest;

import info.novatec.testit.webtester.api.annotations.IdentifyUsing;
import info.novatec.testit.webtester.api.exceptions.PageObjectIsInvisibleException;
import info.novatec.testit.webtester.internal.pageobjects.DefaultPageObjectFactory;
import info.novatec.testit.webtester.internal.pageobjects.PageObjectModel;
import info.novatec.testit.webtester.pageobjects.PageObject;
import info.novatec.testit.webtester.pageobjects.TextField;
import info.novatec.testit.webtester.utils.Identifications;


public class PageObjectIntegrationTest extends AbstractWebTesterIntegrationTest {

    private static final String VISIBLE_CHECKBOX = "visibleCheckbox";
    private static final String INVISIBLE_CHECKBOX = "invisibleCheckbox";

    private static final String VISIBLE_DIV = "visibleDiv";
    private static final String INVISIBLE_DIV = "invisibleDiv";

    private static final String VISIBLE_ELEMENT = "visibleElement";
    private static final String INVISIBLE_ELEMENT = "invisibleElement";
    private static final String ENABLED_ELEMENT = "enabledElement";
    private static final String DISABLED_ELEMENT = "disabledElement";
    private static final String ATTRIBUTED_ELEMENT = "attributedElement";
    private static final String PRESENT_ELEMENT = "presentElement";
    private static final String UNKNOWN_ELEMENT = "unknown";

    @Override
    protected String getHTMLFilePath() {
        return "html/pageobject/base/index.html";
    }

    @Test(expected = NoSuchElementException.class)
    public final void testExistence_ElementDoesNotExist_Exception() {
        PageObject element = getPageObjectForID("unknown element");
        element.click();
    }

    /* testClick */

    @Test
    public final void testClick_VisibleClickableItem_ItemClicked() {
        PageObject element = getPageObjectForID(VISIBLE_CHECKBOX);
        element.click();
        assertThat(element.getAttribute(SELECTED), is(equalTo("true")));
    }

    @Test
    public final void testClick_VisibleNonClickableItem_NothingHappened() {
        PageObject element = getPageObjectForID(VISIBLE_DIV);
        element.click();
    }

    @Test(expected = PageObjectIsInvisibleException.class)
    public final void testClick_InvisibleClickableItem_Exception() {
        PageObject element = getPageObjectForID(INVISIBLE_CHECKBOX);
        element.click();
    }

    @Test(expected = PageObjectIsInvisibleException.class)
    public final void testClick_InvisibleNonClickableItem_Exception() {
        PageObject element = getPageObjectForID(INVISIBLE_DIV);
        element.click();
    }

    /* testGetVisibleText */

    @Test
    public final void testGetVisibleText_SingleElement_WholeText() {
        PageObject element = getPageObjectForID("elementForVisibleText1");
        assertThat(element.getVisibleText(), is(equalTo("element for tag value")));
    }

    @Test
    public final void testGetVisibleText_VisibleNestedElement_WholeText() {
        PageObject element = getPageObjectForID("elementForVisibleText2");
        assertThat(element.getVisibleText(), is(equalTo("element for tag value")));
    }

    @Test
    public final void testGetVisibleText_InvisibleNestedElement_PartOfText() {
        PageObject element = getPageObjectForID("elementForVisibleText3");
        assertThat(element.getVisibleText(), is(equalTo("element value")));
    }

    /* testIsVisible */

    @Test
    public final void testIsVisible_ElementVisible_True() {
        PageObject element = getPageObjectForID(VISIBLE_ELEMENT);
        assertThat(element.isVisible(), is(true));
    }

    @Test
    public final void testIsVisible_ElementInvisible_False() {
        PageObject element = getPageObjectForID(INVISIBLE_ELEMENT);
        assertThat(element.isVisible(), is(false));
    }

    @Test
    public final void testIsPresent_ElementPresent_True() {
        PageObject element = getPageObjectForID(PRESENT_ELEMENT);
        assertThat(element.isPresent(), is(true));
    }

    @Test
    public final void testIsPresent_ElementNotPresent_False() {
        PageObject element = getPageObjectForID(UNKNOWN_ELEMENT);
        assertThat(element.isPresent(), is(false));
    }

    /* testIsEnabled */

    @Test
    public final void testIsEnabled_ElementEnabled_True() {
        PageObject element = getPageObjectForID(ENABLED_ELEMENT);
        assertThat(element.isEnabled(), is(true));
    }

    @Test
    public final void testIsEnabled_ElementDisabled_False() {
        PageObject element = getPageObjectForID(DISABLED_ELEMENT);
        assertThat(element.isEnabled(), is(false));
    }

    /* testGetAttribute */

    @Test
    public final void testGetAttribute_AttributeSet_ValueOfTheAttribute() {
        PageObject element = getPageObjectForID(ATTRIBUTED_ELEMENT);
        assertThat(element.getAttribute("testattribute"), is(equalTo("123")));
    }

    @Test
    public final void testGetAttribute_AttributeNotSet_Null() {
        PageObject element = getPageObjectForID(ATTRIBUTED_ELEMENT);
        assertThat(element.getAttribute(UNKNOWN), is(nullValue()));
    }

    /* setting attribute */

    @Test
    public void setAttribute() {
        TextField textField = getBrowser().create(TestPage.class).forSetAttribute;
        textField.setAttribute("value", "hello world!");
        assertThat(textField.getText(), is(equalTo("hello world!")));
    }

    @Test
    public void setAttributeWithCharactersThatNeedEscaping() {
        TextField textField = getBrowser().create(TestPage.class).forSetAttribute;
        textField.setAttribute("value", "hello \"world!\"");
        assertThat(textField.getText(), is(equalTo("hello \"world!\"")));
    }

    /* testGetCssProperty */

    @Test
    public final void testGetCssProperty_CSSPropertySet_ValueOfTheProperty() {
        PageObject element = getPageObjectForID("cssElement");
        assertThat(element.getCssProperty("border-width"), is(equalTo("1px")));
    }

    /* UTILITIES */

    public PageObject getPageObjectForID(String id) {
        PageObjectModel metaData = PageObjectModel.forPageFragment(getBrowser(), Identifications.id(id), null);
        return new DefaultPageObjectFactory().create(PageObject.class, metaData);
    }

    public static class TestPage extends PageObject {

        @IdentifyUsing("forSetAttribute")
        TextField forSetAttribute;

    }

    public static class InvalidationPageObject extends PageObject {

        @IdentifyUsing("forInvalidation")
        TextField forInvalidation;

        @IdentifyUsing("forInvalidationOfList")
        List<TextField> forInvalidationOfList;

    }

}
