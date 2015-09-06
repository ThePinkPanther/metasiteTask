function ajaxCall(data, type, url) {
    return jQuery.ajax({
        type: type,
        url: url,
        data: data,
        dataType: 'json',
        timeout: 30000,
        async: true,
        cache: false
    });
}

function FilesView(path, element) {
    this.refresh;
    this.refreshDOM;
    this.removeAll;
    this.remove;
    this.path = path;
    this.element = $(element);
    this.container;
    this.refreshInterval = 1500;

    var o = this;

    var container = document.createElement("div");
    container.setAttribute("class", "fileDisplayContainer");
    var removeBtn = document.createElement("div");
    removeBtn.setAttribute("class", "removeAllFiles button");
    removeBtn.textContent = "Remove all"
    this.element.append(removeBtn);
    this.element.append(container);
    this.container = $(container);
    $(removeBtn).click(function () {
        o.removeAll();
    });

    this.refresh();

    setInterval(function () {
        o.refresh()
    }, this.refreshInterval);
}

FilesView.prototype.removeAll = function removeAll() {
    ajaxCall({}, "DELETE", this.path).success(function (o) {
    });
};

FilesView.prototype.remove = function remove(name) {
    ajaxCall({}, "DELETE", this.path + "/" + name).success(function (o) {
    });
};

FilesView.prototype.refreshDOM = function refreshDOM(objects) {
    var DOM = $("<div class='files'></div>");
    var o = this;

    for (var object in objects) {
        var file = $("<div class='file'></div>");
        var link = $("<a href='" + objects[object].url + "'></a>");
        var name = $("<div class='fileName'>" + objects[object].name + "</div>");
        var removeBtn = $("<div id='remove_" + objects[object].name + "' class='removeFileBtn button'>remove</div>");
        file.append(link);
        link.append(name);
        file.append(removeBtn);
        DOM.append(file);
        removeBtn.click(function () {
            o.remove(objects[object].name);
        });
    }

    this.container.html(DOM);
}

FilesView.prototype.refresh = function refresh() {
    var promise = ajaxCall({}, "GET", this.path);
    var p = this;
    promise.success(function (obj) {
        p.refreshDOM(obj);
    })
};

function generateFiles() {
    ajaxCall({}, "GET", "/files/user/generate").sucess(function (o) {

    });
}

$(function () {
    document.userFiles = new FilesView("/files/user", $("#userFilesContainer"));
    document.genFiles = new FilesView("/files/generated", $("#generatedFilesContainer"));

    $('form').on('submit', function uploadFiles(event) {
        event.stopPropagation();
        event.preventDefault();
        var data = new FormData($(this)[0]);
        $.ajax({
            url: '/files/user',
            type: 'POST',
            data: data,
            cache: false,
            processData: false,
            contentType: false
        });
    });

});