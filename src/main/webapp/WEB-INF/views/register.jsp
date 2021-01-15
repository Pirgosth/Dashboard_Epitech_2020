<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Register</title>
    <link rel="stylesheet" href="/libs/bootstrap-5.0.0-beta1-dist/css/bootstrap.css">
    <link rel="stylesheet" href="/libs/fontawesome-free-5.15.1-web/css/all.css">
    <link rel="stylesheet" href="/stylesheets/login.css">
    <link rel="stylesheet" href="/stylesheets/register.css">
</head>
<body>
<div class="container">
    <div class="d-flex justify-content-center h-100">
        <div class="card">
            <div class="card-header">
                <h3>Sign Up</h3>
                <div class="d-flex justify-content-end social_icon">
                    <span><a href="/github/auth"><i class="fab fa-github-square"></i></a></span>
                </div>
            </div>
            <div class="card-body">
                <form id="register-form" novalidate>
                    <div class="input-group form-group mb-3">
                        <span class="input-group-text"><i class="fas fa-user"></i></span>
                        <input name="username" type="text" class="form-control" placeholder="username" required>
                        <div class="invalid-feedback">
                            This field is required
                        </div>
                    </div>
                    <div class="input-group form-group mb-3">
                        <span class="input-group-text"><i class="fas fa-at"></i></span>
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
                    <div class="input-group form-group mb-3">
                        <span class="input-group-text"><i class="fas fa-check"></i></span>
                        <input name="confirm_password" type="password" class="form-control" placeholder="confirm your password" required>
                        <div class="invalid-feedback">
                            This field is required
                        </div>
                    </div>
                    <div class="form-group">
                        <input type="submit" value="Register" class="btn float-end login_btn">
                    </div>
                </form>
            </div>
            <div class="card-footer">
                <div class="d-flex justify-content-center links">
                    Already registered?<a href="/login">Log in</a>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="/libs/jquery-3.5.1.js"></script>
<script src="/libs/bootstrap-5.0.0-beta1-dist/js/bootstrap.js"></script>
<script src="/libs/fontawesome-free-5.15.1-web/js/all.js"></script>
<script src="/scripts/register.js"></script>
</body>
</html>
