$(document).on('DOMNodeInserted', function (e) {
    var observer = new MutationObserver(function (mutations) {
        if ($(target).css("display") === "block") {
            $(".tr-dropdown").width($(".tr-combobox-selected-entry-wrapper").width() - 2);
        }
    });
    var target = document.querySelector('.tr-dropdown');
    observer.observe(target, {
        attributes: true
    });
});