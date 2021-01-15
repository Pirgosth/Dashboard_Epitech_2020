<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="/libs/bootstrap-5.0.0-beta1-dist/css/bootstrap.css">
    <link rel="stylesheet" href="/libs/fontawesome-free-5.15.1-web/css/all.css">
    <link rel="stylesheet" href="/stylesheets/login.css">
</head>
<body>
<div class="container">
    <div class="d-flex justify-content-center h-100">
        <div class="card">
            <div class="card-header">
                <h3>Sign In</h3>
                <div class="d-flex justify-content-end social_icon">
                    <span><a href="/github/auth"><i class="fab fa-github-square"></i></a></span>
                </div>
            </div>
            <div class="card-body">
                <form id="login-form" novalidate>
                    <div class="input-group form-group mb-3">
                        <span class="input-group-text"><i class="fas fa-user"></i></span>
                        <input name="email" type="text" class="form-control" placeholder="email" required>
                        <div class="invalid-feedback">
                            This field is required
                        </div>
                    </div>
                    <div class="input-group form-group mb-3">
                        <span class="input-group-text"><i class="fas fa-key"></i></span>
                        <input name="password" type="password" class="form-control" placeholder="password" required>
                        <div class="invalid-feedback">
                            This field is required
                        </div>
                    </div>
                    <div class="form-group">
                        <input type="submit" value="Login" class="btn float-end login_btn">
                    </div>
                </form>
            </div>
            <div class="card-footer">
                <div class="d-flex justify-content-center links">
                    Don't have an account?<a href="/register">Sign Up</a>
                </div>
                <div class="d-flex justify-content-center">
                    <a href="#">Forgot your password?</a>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="/libs/jquery-3.5.1.js"></script>
<script src="/libs/bootstrap-5.0.0-beta1-dist/js/bootstrap.bundle.js"></script>
<script src="/libs/fontawesome-free-5.15.1-web/js/all.js"></script>
<script src="/scripts/login.js"></script>
</body>
</html>
