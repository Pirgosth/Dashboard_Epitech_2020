class WidgetSettingsModal {
    constructor(title, body, loadCallback, saveCallback) {

        this.title = title;
        this.body = body;
        this.loadCallback = loadCallback;
        this.saveCallback = saveCallback;

    }
}

let widgetsSettingsModal = {
    STEAM_PROFILE: new WidgetSettingsModal("Steam Profile Settings",
        '<div class="row g-3 align-items-center">\n' +
        '    <div class="col-auto">\n' +
        '        <label for="steam-widget-settings-username" class="col-form-label">Steam Username</label>\n' +
        '    </div>\n' +
        '    <div class="col-auto">\n' +
        '        <input type="text" id="steam-widget-settings-username" class="form-control">\n' +
        '    </div>\n' +
        '</div>',
        function (url_parameters) {
            let self = this;
            let username = url_parameters.username;
            if (username === undefined) {
                return;
            }
            $.get("/steam/profile?username=" + username, function (data) {
                let pseudo = data.personaname ?? "No name found";
                let realName = data.realname !== undefined ? "[" + data.realname + "]" : "";
                let profileImgLink = data.avatarfull ?? "#";
                let level = data.level ?? 0;
                let friends = data.friends ?? 0;
                let profileLink = data.profileurl ?? "#";

                self.find(".steam-profile-widget-name").text(pseudo);
                self.find(".steam-profile-widget-real-name").text(realName);
                self.find("img").attr("src", profileImgLink);
                self.find(".steam-profile-widget-level").text(level < 0 ? "unknown" : level);
                self.find(".steam-profile-widget-friends").text(friends < 0 ? "unknown" : friends);
                self.find("a").attr("href", profileLink);
            });
        },
        function (modal) {
            let username = modal.find(".modal-body").find("input").val();
            if (username === undefined) {
                return;
            }
            return {"username": username};
        }),
    STEAM_HOURS: new WidgetSettingsModal("Steam Hours Settings",
        '<div class="row g-3 align-items-center">\n' +
        '    <div class="col-auto">\n' +
        '        <label for="steam-widget-settings-username" class="col-form-label">Steam Username</label>\n' +
        '    </div>\n' +
        '    <div class="col-auto">\n' +
        '        <input type="text" id="steam-widget-settings-username" class="form-control">\n' +
        '    </div>\n' +
        '</div>',
        function (url_parameters) {
            let self = this;
            let username = url_parameters.username;
            if (username === undefined) {
                return;
            }
            $.get("/steam/hours?username=" + username, function (data) {
                let hours = data.hours === undefined ? 0.0 : data.hours;
                self.find(".steam-hours-content").text(hours < 0 ? "unknown" : hours + "h");
                self.find(".steam-hours-username").text("(" + data.username + ")");
            });
        },
        function (modal) {
            let username = modal.find(".modal-body").find("input").val();
            if (username === undefined) {
                return;
            }
            return {"username": username};
        }),
    STEAM_VALUE: new WidgetSettingsModal("Steam Value Settings",
        '<div class="row g-3 align-items-center">\n' +
        '    <div class="col-auto">\n' +
        '        <label for="steam-widget-settings-username" class="col-form-label">Steam Username</label>\n' +
        '    </div>\n' +
        '    <div class="col-auto">\n' +
        '        <input type="text" id="steam-widget-settings-username" class="form-control">\n' +
        '    </div>\n' +
        '</div>',
        function (url_parameters) {
            let self = this;
            let username = url_parameters.username;
            if (username === undefined) {
                return;
            }
            $.get("/steam/value?username=" + username, function (data) {
                let cost = data.cost === undefined ? 0.0 : data.cost;
                self.find(".steam-value-content").text(cost < 0 ? "unknown" : cost + "€");
                self.find(".steam-value-username").text("(" + data.username + ")");
            });
        },
        function (modal) {
            let username = modal.find(".modal-body").find("input").val();
            if (username === undefined) {
                return;
            }
            return {"username": username};
        }),
    YOUTUBE_LATEST_VIDEO: new WidgetSettingsModal("Youtube Latest Video Settings",
        '<div class="row g-3 align-items-center">\n' +
        '    <div class="col-auto">\n' +
        '        <label for="youtube-widget-settings-channel-parameter" class="col-form-label">Youtube channel link</label>\n' +
        '    </div>\n' +
        '    <div class="col-auto">\n' +
        '        <input type="text" id="youtube-widget-settings-channel-parameter" class="form-control">\n' +
        '    </div>\n' +
        '</div>',
        function (url_parameters) {
            let self = this;
            let channelParameter = url_parameters.channelParameter;
            if (channelParameter === undefined) {
                return;
            }
            $.get("/youtube/latest?channelParameter=" + channelParameter, function (data) {
                let iframeContent = data.videoPlayer === undefined ? "" : data.videoPlayer;
                let videoFrame = $(iframeContent);
                self.find(".youtube-latest-widget-channel-name").text(`Latest ${data.channelName}'s video`);
                self.find("iframe").remove();
                self.find(".youtube-latest-widget-content").append(videoFrame);
            });
        },
        function (modal) {
            let channelParameter = modal.find(".modal-body").find("input").val();
            if (channelParameter === undefined) {
                return;
            }
            return {"channelParameter": channelParameter};
        }),
    YOUTUBE_CHANNEL_STATS: new WidgetSettingsModal("Youtube Channel Statistics Settings",
        '<div class="row g-3 align-items-center">\n' +
        '    <div class="col-auto">\n' +
        '        <label for="youtube-widget-settings-channel-parameter" class="col-form-label">Youtube channel link</label>\n' +
        '    </div>\n' +
        '    <div class="col-auto">\n' +
        '        <input type="text" id="youtube-widget-settings-channel-parameter" class="form-control">\n' +
        '    </div>\n' +
        '</div>',
        function (url_parameters) {
            let self = this;
            let channelParameter = url_parameters.channelParameter;
            if (channelParameter === undefined) {
                return;
            }
            $.get("/youtube/profile?channelParameter=" + channelParameter, function (data) {
                let channelName = data.channelName !== undefined ? data.channelName : "No name found";
                let channelImgUrl = data.thumbnails !== undefined ? data.thumbnails.high.url : "#";
                let channelStats = data.statistics !== undefined ? data.statistics : {
                    viewCount: -1,
                    subscriberCount: -1,
                    videoCount: -1
                };

                self.find(".youtube-channel-stats-widget-channel-name").text(channelName);
                self.find(".youtube-channel-stats-widget-channel-img").attr("src", channelImgUrl);
                self.find(".youtube-channel-stats-widget-channel-subscribers").text(formatNumber(channelStats.subscriberCount));
                self.find(".youtube-channel-stats-widget-channel-views").text(formatNumber(channelStats.viewCount));
                self.find(".youtube-channel-stats-widget-channel-videos").text(formatNumber(channelStats.videoCount));
            });
        },
        function (modal) {
            let channelParameter = modal.find(".modal-body").find("input").val();
            if (channelParameter === undefined) {
                return;
            }
            return {"channelParameter": channelParameter};
        }),
    GITHUB_REPOS: new WidgetSettingsModal("Github Repositories Settings",
        '<div class="row g-3 align-items-center">\n' +
        '    <div class="col-auto">\n' +
        '        <label for="github-widget-settings-username-parameter" class="col-form-label">Github Username</label>\n' +
        '    </div>\n' +
        '    <div class="col-auto">\n' +
        '        <input type="text" id="github-widget-settings-username-parameter" class="form-control">\n' +
        '    </div>\n' +
        '</div>',
        function (url_parameters) {
            let self = this;
            let username = url_parameters.username;
            if (username === undefined) {
                return;
            }
            let repositoryTemplate = '<li class="list-group-item">\n' +
                '                <div class="github-repos-widget-repo-left">\n' +
                '                    <h4>\n' +
                '                        <a class="github-repos-widget-repo-url" href="#"><span class="github-repos-widget-repo-name"></span></a> <span class="github-repos-widget-repo-language fs-5"></span><i class="github-repos-widget-repo-private fas fa-lock"></i>\n' +
                '                    </h4>\n' +
                '                    <p class="github-repos-widget-repo-description"></p>\n' +
                '                </div>\n' +
                '                <div class="github-repos-widget-repo-right">\n' +
                '                    <div class="input-group mb-3">\n' +
                '                        <span class="input-group-text">Clone</span>\n' +
                '                        <input type="text" class="form-control" readonly>\n' +
                '                    </div>\n' +
                '                </div>\n' +
                '            </li>';
            $.get("/github/repos?username=" + username, function (data) {
                self.find(".github-repos-widget-repo-list > li").remove();
                let githubUsername = data.username ?? "";
                self.find(".github-repos-widget-owner-name").text(githubUsername + "'s Repositories");
                data.repositories.forEach(repository => {
                    let name = repository.name ?? "Name not found";
                    let url = repository.html_url ?? "#";
                    let language = "(" + (repository.language !== undefined ? repository.language : "No language defined") + ")";
                    let isPrivate = repository.private ?? false;
                    let description = repository.description ?? "";
                    let cloneUrl = repository.clone_url ?? "";

                    let currentRepository = $(repositoryTemplate);
                    currentRepository.find(".github-repos-widget-repo-name").text(name);
                    currentRepository.find(".github-repos-widget-repo-url").attr("href", url);
                    currentRepository.find(".github-repos-widget-repo-language").text(language);
                    if (!isPrivate) {
                        currentRepository.find(".github-repos-widget-repo-private").remove();
                    }
                    currentRepository.find(".github-repos-widget-repo-description").text(description);
                    currentRepository.find("input").val(cloneUrl);

                    self.find(".github-repos-widget-repo-list").append(currentRepository);

                });
            });

        }, function (modal) {
            let username = modal.find("input").val();
            if (username === undefined) {
                return;
            }
            return {username: username};
        }),
    GITHUB_FOLLOWERS: new WidgetSettingsModal("Github Followers Settings",
        '<div class="row g-3 align-items-center">\n' +
        '    <div class="col-auto">\n' +
        '        <label for="github-widget-settings-username-parameter" class="col-form-label">Github Username</label>\n' +
        '    </div>\n' +
        '    <div class="col-auto">\n' +
        '        <input type="text" id="github-widget-settings-username-parameter" class="form-control">\n' +
        '    </div>\n' +
        '</div>',
        function (url_parameters) {
            let self = this;
            let username = url_parameters.username;
            if (username === undefined) {
                return;
            }
            let followerTemplate = '<li class="list-group-item">\n' +
                '            <div class="github-followers-widget-follower">\n' +
                '                <h4>\n' +
                '                    <a class="github-follower-widget-follower-profile-url" href="#">\n' +
                '                        <img alt="Profile Img" src="#" class="github-followers-widget-follower-img-url">\n' +
                '                    </a>\n' +
                '                    <span class="github-followers-widget-follower-name"></span>\n' +
                '                </h4>\n' +
                '            </div>\n' +
                '        </li>';
            $.get("/github/followers?username=" + username, function (data) {
                let githubUsername = data.username;
                self.find(".github-followers-widget-name").text(githubUsername + "'s Followers");
                self.find(".github-followers-widget-followers-list > li").remove();

                data.followers.forEach(follower => {
                    let currentFollower = $(followerTemplate);
                    let name = follower.login ?? "Username not found";
                    let avatarUrl = follower.avatar_url ?? "#";
                    let profileUrl = follower.html_url ?? "#";

                    currentFollower.find(".github-followers-widget-follower-name").text(name);
                    currentFollower.find(".github-followers-widget-follower-img-url").attr("src", avatarUrl);
                    currentFollower.find(".github-follower-widget-follower-profile-url").attr("href", profileUrl);

                    self.find(".github-followers-widget-followers-list").append(currentFollower);

                });
            });
        }, function (modal){
            let username = modal.find("input").val();
            if (username === undefined) {
                return;
            }
            return {username: username};
        })
};

function formatNumber(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
}

function getWidgetSettingsModal(widget) {
    let widgetName = $(widget).attr("data-widget-name");
    return widgetsSettingsModal[widgetName];
}

function getWidgetLoadCallback(widget) {
    let widgetName = $(widget).attr("data-widget-name");
    let widgetSettingsModal = widgetsSettingsModal[widgetName]
    return widgetSettingsModal !== undefined ? widgetSettingsModal.loadCallback : undefined;
}

function fillWidgetSettingsModal(modal, widget) {

    let widgetSettingsModal = getWidgetSettingsModal(widget);

    if (widgetSettingsModal === undefined) {
        return;
    }

    let saveCallback = widgetSettingsModal.saveCallback;

    modal.find(".modal-title").text(widgetSettingsModal.title);
    modal.find(".modal-body").html(widgetSettingsModal.body);
    modal.find(".btn-primary").off();
    modal.find(".btn-primary").click(function () {
        if (saveCallback !== undefined) {
            let url_parameters = saveCallback.call(widget, modal);
            updateWidget(widget.get(0), undefined, url_parameters).then(() => {
                loadWidgetParameters(widget, url_parameters);
                modal.modal("hide");
            });
        } else {
            modal.modal("hide");
        }
    });

    modal.modal("show");

}

$(document).ready(function () {

});