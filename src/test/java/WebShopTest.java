import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class WebShopTest {
    @BeforeAll
    static void setUpConfig() {
        Configuration.browser = "chrome";
        //  Configuration.startMaximized = true;
    }

    @Test
    public void addToCartTest() {

        String cookie = given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body("addtocart_31.EnteredQuantity=1")
                .when()
                .post("http://demowebshop.tricentis.com/addproducttocart/details/31/1")
                .then()
                .statusCode(200)
                .body("success", is(true),
                        "message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"),
                        "updatetopcartsectionhtml", is("(1)"))
                .extract().cookie("Nop.customer");

        open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png");
        getWebDriver().manage().addCookie(
                new Cookie("Nop.customer", cookie));

        open("http://demowebshop.tricentis.com/");
        $(".ico-cart .cart-qty").shouldHave(Condition.text("(1)"));
    }
}
