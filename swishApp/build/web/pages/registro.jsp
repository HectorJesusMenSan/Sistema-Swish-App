<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Registro</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/base.css">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/components.css">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/layout.css">
        <style>
            .login-card {
                background: rgba(28,28,28,0.9);
                border: 1px solid rgba(255,255,255,0.08);
                border-radius: 20px;
                padding: 2rem;
                margin-top: 2rem;
            }
            .titulo {
                color: #f47b25;
                font-weight: 800;
                text-align: center;
                font-size: 1.6rem;
            }
            input.form-control {
                background: rgba(255,255,255,0.08) !important;
                border: 1px solid rgba(255,255,255,0.1) !important;
                color: white !important;
                border-radius: 10px;
                padding: 0.75rem;
            }
            input.form-control:focus {
                border-color: #f47b25 !important;
                box-shadow: 0 0 0 2px rgba(244,123,37,0.3) !important;
            }
            .btn-registro {
                background: #f47b25;
                color: white;
                border: none;
                border-radius: 12px;
                padding: 0.75rem;
                font-weight: bold;
                width: 100%;
                font-size: 1rem;
            }
            .btn-registro:hover {
                background: #ff8a3d;
            }
            .link-login {
                color: #f47b25;
                text-align: center;
                display: block;
                margin-top: 1rem;
                text-decoration: none;
            }
            .link-login:hover {
                color: #ff8a3d;
            }
        </style>
    </head>
    <body>
        <div class="container" style="max-width:400px;">
            <div class="login-card">

                <h2 class="titulo">Crear Cuenta</h2>

                <% if (request.getAttribute("error") != null) {%>
                <div class="alert alert-danger mt-3">
                    <%= request.getAttribute("error")%>
                </div>
                <% }%>

                <form action="<%= request.getContextPath()%>/LoginServlet"
                      method="post" class="mt-3">

                    <input type="hidden" name="accion" value="registro">

                    <div class="mb-3">
                        <label class="form-label">Nombre completo</label>
                        <input type="text" name="nombre"
                               class="form-control" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Usuario</label>
                        <input type="text" name="username"
                               class="form-control" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Contraseña</label>
                        <input type="password" name="password"
                               class="form-control" required minlength="6">
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Confirmar contraseña</label>
                        <input type="password" name="confirmar"
                               class="form-control" required>
                    </div>

                    <button type="submit" class="btn-registro">
                        Crear Cuenta
                    </button>

                </form>

                <a href="<%= request.getContextPath()%>/LoginServlet"
                   class="link-login">
                    ← Volver al login
                </a>

            </div>
        </div>
    </body>
</html>