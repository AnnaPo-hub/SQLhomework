package ru.netology.pageObject.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement firstCardButton = $$("[data-test-id=action-deposit]").first();

}
