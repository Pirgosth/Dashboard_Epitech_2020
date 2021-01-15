$(document).ready(function () {

    $("#login-form").submit(function (event) {

        let self = this;
        event.preventDefault();
        $(this).addClass('was-validated');

        $(this).find("input").get().forEach(e => {
            e.setCustomValidity("");
            if ($(e).val() === "" && $(e).prop("required")) {
                $(e).parent().find(".invalid-feedback").text("This field is required");
            }
        });

        if (!this.checkValidity()) {
            return;
        }

        $.post({
            url: "/login",
            data: $(this).serialize(),
            success: function (data) {
                console.log(data);
                if (data.success) {
                    setTimeout(() => window.location = "/index", 200);
                } else {
                    for (let field of data.fields) {
                        if (!field.validity) {
                            $(self).find(`input[name="${field.name}"]`).parent().find(".invalid-feedback").text(field.cause);
                            $(self).find(`input[name="${field.name}"]`).get(0).setCustomValidity("false");
                        }
                    }
                }
            }
        });
    });

});