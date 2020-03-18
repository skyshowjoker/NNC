<%@ page import="nnc.utils.DbConn" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="nnc.been.Music" %>
<%@ page import="java.util.List" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="nnc.utils.FindService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="description" content="Dropcast is a responsive HTML/CSS/Javascript template for podcasts">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>home</title>
    <script type="text/javascript" src="http://www.daimajiayuan.com/download/jquery/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" src="http://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/bootstrap-select.js"></script>
    <link rel="stylesheet" type="text/css" href="http://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/css/bootstrap-select.css">



    <!-- 3.0 -->
    <link href="http://netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css" rel="stylesheet">
    <script src="http://netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>

    <link rel="stylesheet" href="assets/css/dropcast.css">
    <link rel="author" href="Amie Chen for Codrops">
    <link href="https://use.fontawesome.com/releases/v5.0.6/css/all.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Rufina:400,700|Source+Sans+Pro:200,300,400,600,700" rel="stylesheet">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/particles.js/2.0.0/particles.min.js"></script>

</head>
<body>
<%
    String sqls="select music.* from music";
    DbConn db=new DbConn();
    ResultSet rs=db.query(sqls);
    List<Music> alist= FindService.getMusic(rs);
    request.setAttribute("alist", alist);
    int i=0;
%>
<div id="site__bg"></div>
<div class="main">
    <nav>
        <div class="logo nav__logo">
            <a href="home.jsp"><img src="assets/images/logo.svg" alt="logo"/></a>
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
    </section>
    <section class="episodes">
        <c:forEach var="a" items="${alist}">
            <article class="episode">
                <h2 class="episode__number">${a.id}</h2>
                <div class="episode__media">
                    <a href="DownloadSvlt?name=${a.name}" class="episode__image"><img class="episode__image" src="assets/images/photos/original/6.jpg"></a>
                </div>
                <div class="episode__detail">
                    <a href="DownloadSvlt?name=${a.name}" class="episode__title"><h4>${a.name}</h4></a>
                    <p class="episode__description">The emotion of this music is
                        <%

                            if("1".equals(alist.get(i).getMotion())){
                        %>
                        positive
                        <%
                        }else if("2".equals(alist.get(i).getMotion())){
                        %>
                        negative
                        <%
                        }else{
                        %>

                        random
                        <%

                            }
                        %>
                        and the mode is
                        <%

                            if("-1".equals(alist.get(i).getMode())){
                        %>
                        random
                        <%
                        }else{
                        %>
                            ${a.mode}
                        <%
                            }
                        %>
                        . The meter and the basic meter are
                        <%

                            if("-1".equals(alist.get(i).getBeat())){
                        %>
                        random
                        <%
                        }else{
                        %>
                            ${a.beat}
                        <%
                            }

                        %> and
                        <%

                            if("-1".equals(alist.get(i).getBasicbeat())){
                        %>
                        random
                        <%
                        }else{
                        %>
                            ${a.basicbeat}
                        <%
                            }
                            i++;
                        %>
                        , respectively.</p>
                </div>
            </article>
        </c:forEach>
        <article class="episode">
            <h2 class="episode__number"></h2>
            <div class="episode__media">
                <a href="form.jsp" class="episode__image"><img class="episode__image" src="assets/images/photos/original/add.jpg"></a>
            </div>
            <div class="episode__detail">
                <a href="home.jsp" class="episode__title"><h4>add</h4></a>
                <p class="episode__description"></p>
            </div>
        </article>
        <article class="episode">
            <h2 class="episode__number"></h2>
            <div class="episode__media">
                <a href="DeleteAll" class="episode__image"><img class="episode__image" src="assets/images/photos/original/rm.jpg"></a>
            </div>
            <div class="episode__detail">
                <a href="DeleteAll" class="episode__title"><h4>remove all</h4></a>
                <p class="episode__description"></p>
            </div>
        </article>


    </section>
</div>

<script src="assets/js/dropcast.js"></script>
</body>
</html>