// import GridStack from "/libs/gridstack/gridstack-dd-native";

function registerWidget(name, widget) {
    return new Promise(resolve => {
        $.post({
            url: "/widgets/create",
            contentType: "application/json",
            data: JSON.stringify({
                "name": name,
                "parameters": {
                    "x": widget.gridstackNode.x,
                    "y": widget.gridstackNode.y,
                    "w": widget.gridstackNode.w,
                    "h": widget.gridstackNode.h
                }
            }),
            success: function (data) {
                return resolve(data.id !== undefined ? data.id : -1);
            }
        });
    });
}

function unregisterWidget(widget) {

    return new Promise(resolve => {
        let widgetId = widget.id;
        if (widgetId === undefined) {
            return resolve(false);
        }
        $.ajax({
            url: "/widgets/delete",
            method: "delete",
            contentType: "application/json",
            data: JSON.stringify({"id": widgetId}),
            success: function () {
                return resolve(true);
            }
        });

    });

}

function loadExistingWidgets(grid) {
    $.get("/widgets/list", function (data) {
        if (!Array.isArray(data)) {
            return;
        }
        data.forEach(e => {
            let parameters = e.parameters;
            parameters.content = `<div>Loading ${e.name}</div>`
            let widget = grid.addWidget(e.parameters);
            widget.id = e.id;
            $(widget).attr("data-widget-name", e.name);
            loadWidgetCard(e.route, widget).then(() => {

                loadWidgetParameters(widget, e.url_parameters !== undefined ? JSON.parse(e.url_parameters) : {});

            });
        });
    });
}

function getWidgetById(widgets, id){
    for(let widget of widgets){
        if(widget.id !== undefined && Number(widget.id) === id){
            return widget;
        }
    }
    return undefined;
}

function refreshWidgets(grid){
    $.get("/widgets/list", function (data){
        if (!Array.isArray(data)) {
            return;
        }
        let widgets = grid.getGridItems();
        data.forEach(e => {
            let id = e.id;
            let currentWidget = getWidgetById(widgets, id);
            if(currentWidget !== undefined && currentWidget.getAttribute("data-widget-name") !== "YOUTUBE_LATEST_VIDEO"){
                loadWidgetParameters(currentWidget, e.url_parameters !== undefined ? JSON.parse(e.url_parameters) : {});
            }
        });
    });
}

function loadWidgetCard(route, widget) {
    return new Promise(resolve => {
        $.get(route, function (data) {
            $(widget).find(".grid-stack-item-content").html(data);
            widget.gridstackNode.content = data;
            return resolve();
        });
    });
}

function loadWidgetParameters(widget, url_parameters) {
    let loadWidgetCallback = getWidgetLoadCallback(widget);
    if (loadWidgetCallback !== undefined) {
        loadWidgetCallback.call($(widget), url_parameters);
    }
}

function updateWidget(widget, parameters, url_parameters) {
    return new Promise(resolve => {
        if (widget.id === undefined) {
            return resolve();
        }
        $.ajax({
            url: "/widgets/update",
            method: "put",
            contentType: "application/json",
            data: JSON.stringify({
                "id": widget.id,
                "parameters": parameters,
                "url_parameters": JSON.stringify(url_parameters)
            }),
            success: function () {
                return resolve();
            },
        });
    });

}

$(document).ready(function () {

    $('#sidebarCollapse').click(function () {
        $('#sidebar').toggleClass('active');
    });

    GridStack.init({float: true});

    let grid = GridStack.init();

    setInterval(function (){
        refreshWidgets(grid);
    }, 20000);

    grid.on('change', function (event, widgets) {
        widgets.forEach(node => {
            let widget = node.el;
            updateWidget(widget, {x: node.x, y: node.y, w: node.w, h: node.h}, undefined).then();
        });
    });

    loadExistingWidgets(grid);

    $(".add-widget-button").click(function () {
        let clicked = $(this);
        let widgetName = clicked.attr("data-widget-name");
        if (widgetName === undefined) {
            return;
        }
        let newWidget = grid.addWidget({w: 2, h: 3, content: `<div>Loading ${widgetName}</div>`});
        $(newWidget).attr("data-widget-name", widgetName);
        $.get("/widgets/get_route?name=" + widgetName, function (data) {
            if (data === undefined) {
                return;
            }
            loadWidgetCard(data.route, newWidget).then(() => {
                registerWidget(widgetName, newWidget).then(widgetId => {
                    if(widgetId >= 0){
                        newWidget.id = widgetId;
                    }
                });
            });
        });
    });

    $(document).on("click", ".open-widget-settings", function () {
        let clicked = $(this);
        let widget = clicked.parents(".grid-stack-item");
        let settingsModal = $("#settings-modal");

        fillWidgetSettingsModal(settingsModal, widget);
    });

    $(document).on("click", ".remove-widget", function () {
        let clicked = $(this);
        let widget = clicked.parents(".grid-stack-item");
        grid.removeWidget(widget.get(0));
        unregisterWidget(widget.get(0)).then();
    });

});