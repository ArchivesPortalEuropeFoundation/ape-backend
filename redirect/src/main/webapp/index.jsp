<html>
<head>
    <title>APEF Redirection Service</title>

    <link href="<%= request.getContextPath()%>/assets/image/favicon.ico" rel="Shortcut Icon" />

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@300;400;600&display=swap" rel="stylesheet">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js"></script>

    <style>
        @media (min-width: 768px) {
            .h-md-100 {
                height: 100%;
            }
        }
        @media (min-width: 992px) {
            .h-lg-100 {
                height: 100%;
            }
        }
        @media (min-width: 1200px) {
            .h-xl-100 {
                height: 100%;
            }
        }
    </style>
</head>
<body style="font-family: 'Open Sans', sans-serif; ">
    <div class=".container-fluid">
        <div class="col-md-auto col-12 d-xl-none d-block h-50" style="text-align: center">
            <img style="height: 100%" src="<%= request.getContextPath()%>/assets/image/bg.png"/>
        </div>
        <div class="row align-items-center h-xl-100">
            <div class="col-xl-auto col-12 d-xl-block d-none">
                <img class="h-100" src="<%= request.getContextPath()%>/assets/image/bg.png"/>
            </div>
            <div class="col mt-4 mt-xl-0">
                <div class="row">
                    <div class="col align-self-center" style="text-align: center; color: #b23063">
                        <h1>APEF Redirection Service</h1>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
