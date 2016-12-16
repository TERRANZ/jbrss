<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, user-scalable=no">
    <link href="http://jqmdesigner.appspot.com/gk/lib/jquery.mobile/1.4.5/jquery.mobile-1.4.5.min.css" rel="stylesheet"
          type="text/css"/>
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script>
        $(document).on("mobileinit", function () {
            $.mobile.autoInitializePage = false;
            $.mobile.hashListeningEnabled = false;
        });

        function mobileInitPage() {
            $.mobile.hashListeningEnabled = true;
            $.mobile.initializePage();
        }
    </script>
    <script src="//code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
    <!-- Uncomment the following to include cordova in your android project -->
    <!--<script src="http://jqmdesigner.appspot.com/platforms/android/cordova.js"></script>-->
    <!-- GK Loader use RequireJS to load module -->
    <script src="http://requirejs.org/docs/release/2.1.11/minified/require.js"></script>
    <!--Plug in GK-->
    <script src="http://jqmdesigner.appspot.com/components/jquery.gk/jquery.gk.min.js"></script>
    <!-- Load GK components -->
    <script components="http://jqmdesigner.appspot.com/components/gk-jquerymobile/content.html,http://jqmdesigner.appspot.com/components/gk-jquerymobile/page.html"
            callback="mobileInitPage" src="http://jqmdesigner.appspot.com/components/gk-loader/gk-loader.js"></script>
    <script src="/js/socialite.js"></script>
    <title>JBRSS App</title>
</head>
<body gk-app>