
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="description" content="Dropcast is a responsive HTML/CSS/Javascript template for podcasts">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>form</title>
    <link rel="stylesheet" href="assets/css/dropcast.css">
    <link rel="author" href="Amie Chen for Codrops">
    <link href="https://use.fontawesome.com/releases/v5.0.6/css/all.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Rufina:400,700|Source+Sans+Pro:200,300,400,600,700" rel="stylesheet">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/particles.js/2.0.0/particles.min.js"></script>
    <link href="assets/css/style.css" rel="stylesheet" type="text/css" media="all" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <!-- -->
    <script>var __links = document.querySelectorAll('a');function __linkClick(e) { parent.window.postMessage(this.href, '*');} ;for (var i = 0, l = __links.length; i < l; i++) {if ( __links[i].getAttribute('data-t') == '_blank' ) { __links[i].addEventListener('click', __linkClick, false);}}</script>
    <script src="js/jquery.min.js"></script>
    <script>$(document).ready(function(c) {
        $('.alert-close').on('click', function(c){
            $('.message').fadeOut('slow', function(c){
                $('.message').remove();
            });
        });
    });
    </script>
    <style type="text/css">

        @import url(css/select2css.css);

    </style>
    <script type="text/javascript" src="js/select2css.js"></script>
</head>
<body>
<div id="site__bg"></div>
<div class="main">
    <nav>
        <div class="logo nav__logo">
            <a href="home.html"><img src="assets/images/logo.svg" alt="logo"/></a>
        </div>
        <ul class="nav__list">
            <li class="nav__item"><a href="#">Homepage</a></li>
        </ul>
        <ul class="nav__social">
            <li><a href="#" class="nav_social__item"><i class="fab fa-google-play"></i></a></li>
            <li><a href="#" class="nav_social__item"><i class="fab fa-itunes-note"></i></a></li>
            <li><a href="#" class="nav_social__item"><i class="fas fa-rss-square"></i></a></li>
        </ul>
    </nav>
    <section class="site">
        <h1 class="site__title site__title--separator">Composition</h1>
        <p class="site__description">A composition web page based on neural network</p>
        <div class="message warning">
            <div class="inset">
                <div class="login-head">
                    <h1>New Item</h1>
                    <div class="alert-close"> </div>
                </div>
                <form action="Add" method="post">

                    <li>
                        <input name="name" type="text" class="text" value="Music name" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'Music name';}"><!-- <a href="#" class=" icon user"></a> -->
                    </li>
                    <div class="clear"> </div>
                    <li>
                        <div id="uboxstyle">
                            <h3 style="color:#32434e;font-size:14px;">max length（最大长度）
                                <select name="maxlength" >
                                    <option value="300" selected>300</option>
                                    <option value="400" >400</option>
                                    <option value="500" >500</option>
                                    <option value="600" >600</option>
                                    <option value="700" >700</option>
                                    <option value="800" >800</option>
                                </select>
                            </h3>

                        </div>
                    </li>

                    <li>
                        <h3 style="color:#32434e;font-size:14px;">emotion（情感倾向）
                            <select name="motion">
                                <option value="1" selected>积极</option>
                                <option value="2" >消极</option>
                                <option value="-1" >随机</option>
                            </select>
                        </h3>
                    </li>
                    <li>
                        <h3 style="color:#32434e;font-size:14px;">beat（节拍）
                            <select name="beat">
                                <option value="4/4" selected>4/4</option>
                                <option value="6/8" >6/8</option>
                                <option value="9/8" >9/8</option>
                                <option value="6/4" >6/4</option>
                                <option value="5/4" >5/4</option>
                                <option value="-1" >随机</option>
                            </select>
                        </h3>
                    </li>
                    <li>
                        <h3 style="color:#32434e;font-size:14px;">basic beat（基本拍）
                            <select name="basicbeat">
                                <option value="1/8" selected>1/8</option>
                                <option value="1/4" >1/4</option>
                                <option value="-1" >随机</option>

                            </select>
                        </h3>
                    </li>
                    <li>
                        <h3 style="color:#32434e;font-size:14px;">mode（调式）
                            <select name="mode">
                                <option value="D" selected>D</option>
                                <option value="Bb" >Bb</option>
                                <option value="A" >A</option>
                                <option value="Am" >Am</option>
                                <option value="Dm" >Dm</option>
                                <option value="G" >G</option>
                                <option value="Am" >Am</option>
                                <option value="C" >C</option>
                                <option value="Gm" >Gm</option>
                                <option value="Em" >Em</option>
                                <option value="E" >E</option>
                                <option value="F" >F</option>
                                <option value="-1" >随机</option>
                            </select>
                        </h3>
                    </li>


                    <div class="clear"> </div>
                    <div class="submit">
                        <input type="submit" onclick="" value="Make" >
                        <div class="clear">  </div>
                    </div>

                </form>
            </div>
        </div>
    </section>



</div>

<script src="assets/js/dropcast.js"></script>
</body>
</html>
