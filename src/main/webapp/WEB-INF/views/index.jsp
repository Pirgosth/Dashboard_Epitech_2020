<%--@elvariable id="isLogged" type="java.lang.Boolean"--%>
<%--@elvariable id="username" type="java.lang.String"--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Dashboard</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="/libs/bootstrap-5.0.0-beta1-dist/css/bootstrap.css">
    <link rel="stylesheet" href="/libs/fontawesome-free-5.15.1-web/css/all.css">
    <link rel="stylesheet" href="/libs/gridstack/gridstack.min.css">
    <link rel="stylesheet" href="/stylesheets/widgets.css">
    <link rel="stylesheet" href="/stylesheets/index.css">
</head>
<body>

<div class="wrapper">
    <!-- Sidebar -->
    <nav id="sidebar">
        <div class="sidebar-header">
            <h3>Epitech Dashboard</h3>
        </div>
        <div id="sidebar-subheader">
            <% if ((boolean) request.getAttribute("isLogged")) {%>
            <div id="sidebar-subheader-credentials">
                <div id="sidebar-subheader-username">Welcome <span>${username}</span></div>
            </div>
            <% } %>
            <div id="sidebar-subheader-title">Choose your widgets</div>
        </div>
        <ul class="list-unstyled components">
            <li class="active" id="steam-service">
                <a class="clickable" data-bs-toggle="collapse" aria-expanded="false" data-bs-target="#steamSubmenu"
                   aria-controls="steamSubmenu"><span class="icon"><i class="fab fa-steam fa-lg"></i></span>Steam</a>
                <ul class="collapse list-unstyled" id="steamSubmenu">
                    <li>
                        <div id="steam-1" class="widget-toggle">
                            <span><span data-widget-name="STEAM_PROFILE" class="add-widget-button"><i
                                    class="fas fa-plus-circle"></i></span>Profile</span>
                        </div>
                    </li>
                    <li>
                        <div id="steam-2" class="widget-toggle">
                            <span><span data-widget-name="STEAM_VALUE" class="add-widget-button"><i
                                    class="fas fa-plus-circle"></i></span>Library value</span>
                        </div>
                    </li>
                    <li>
                        <div id="steam-3" class="widget-toggle">
                            <span><span data-widget-name="STEAM_HOURS" class="add-widget-button"><i
                                    class="fas fa-plus-circle"></i></span>Time played</span>
                        </div>
                    </li>
                </ul>
            </li>
            <li class="active" id="youtube-service">
                <a class="clickable" data-bs-toggle="collapse" data-bs-target="#youtubeSubmenu"
                   aria-expanded="false" aria-controls="youtubeSubmenu"><span class="icon"><i
                        class="fab fa-youtube fa-lg"></i></span>Youtube</a>
                <ul class="collapse list-unstyled" id="youtubeSubmenu">
                    <li>
                        <div id="youtube-1" class="widget-toggle">
                            <span><span data-widget-name="YOUTUBE_CHANNEL_STATS" class="add-widget-button"><i
                                    class="fas fa-plus-circle"></i></span>Channel stats</span>
                        </div>
                    </li>
                    <li>
                        <div id="youtube-2" class="widget-toggle">
                            <span><span data-widget-name="YOUTUBE_LATEST_VIDEO" class="add-widget-button"><i
                                    class="fas fa-plus-circle"></i></span>Latest video</span>
                        </div>
                    </li>
                </ul>
            </li>
            <li class="active" id="github-service">
                <a class="clickable" data-bs-toggle="collapse" data-bs-target="#githubSubmenu"
                   aria-expanded="false" aria-controls="githubSubmenu"><span class="icon"><i
                        class="fab fa-github fa-lg"></i></span>Github</a>
                <ul class="collapse list-unstyled" id="githubSubmenu">
                    <li>
                        <div id="github-1" class="widget-toggle">
                            <span><span data-widget-name="GITHUB_REPOS" class="add-widget-button"><i
                                    class="fas fa-plus-circle"></i></span>List repositories</span>
                        </div>
                    </li>
                    <li>
                        <div id="github-2" class="widget-toggle">
                            <span><span data-widget-name="GITHUB_FOLLOWERS" class="add-widget-button"><i
                                    class="fas fa-plus-circle"></i></span>Followers</span>
                        </div>
                    </li>
                </ul>
            </li>
        </ul>
    </nav>

    <div id="content">
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <div class="container-fluid">
                <button id="sidebarCollapse" class="btn">
                    <i class="fas fa-bars"></i>
                    <span>Toggle Services</span>
                </button>
                <% if (!(boolean) request.getAttribute("isLogged")) { %>
                <span>Login <a href="/login"><i class="fas fa-sign-in-alt"></i></a></span>
                <% } else { %>
                <span>Logout <a href="/logout"><i class="fas fa-sign-out-alt"></i></a></span>
                <% } %>
            </div>
        </nav>
        <h1 class="text-center">Welcome to our dashboard !</h1>
        <p class="text-center">
            This is a random value: <%= Math.random() %>
        </p>

        <div class="grid-stack"></div>

    </div>

    <div class="modal fade" id="settings-modal" tabindex="-1" aria-labelledby="settings-modal-label" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="settings-modal-label">Title</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary">Save changes</button>
                </div>
            </div>
        </div>
    </div>

</div>

<script src="/libs/jquery-3.5.1.js"></script>
<script src="/libs/bootstrap-5.0.0-beta1-dist/js/bootstrap.bundle.js"></script>
<script src="/libs/fontawesome-free-5.15.1-web/js/all.js"></script>
<script src="/libs/gridstack/gridstack-h5.js"></script>
<script src="/scripts/widgets.js"></script>
<script src="/scripts/index.js"></script>
</body>
</html>
