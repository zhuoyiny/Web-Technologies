<?php

if(isset($_GET["key"])){
    $data_arr1 = array();
    $id = $_GET['key'];
    $id = urlencode($id);
    $url2 = "https://maps.googleapis.com/maps/api/place/details/json?placeid=$id&key=AIzaSyCAvB0UHhavDE5H4j1eBotggoeBV69Z0Jo";
    $resp_json2 = file_get_contents($url2);
    $resp2 = json_decode($resp_json2, true);

    $author_name_arr = array();
    $profile_photo_url_arr = array();
    $text_arr = array();
    $photo_height_arr = array();
    $photo_reference_arr = array();
    $photo_width_arr = array();

    if($resp2['status']=='OK'){
        $len = count($resp2['result']);
        for($i = 0; $i<$len; $i++) {

            $author_name = isset($resp2['result']['reviews'][$i]['author_name']) ? $resp2['result']['reviews'][$i]['author_name'] : "";
            array_push($author_name_arr, $author_name);
            $profile_photo_url = isset($resp2['result']['reviews'][$i]['profile_photo_url']) ? $resp2['result']['reviews'][$i]['profile_photo_url'] : "";
            array_push($profile_photo_url_arr, $profile_photo_url);
            $text = isset($resp2['result']['reviews'][$i]['text']) ? $resp2['result']['reviews'][$i]['text'] : "";
            array_push($text_arr, $text);
        }

        for($j = 0; $j<5; $j++){
            $photo_height = isset($resp2['result']['photos'][$j]['height']) ? $resp2['result']['photos'][$j]['height'] : "";
            array_push($photo_height_arr, $photo_height);
            $photo_width = isset($resp2['result']['photos'][$j]['width']) ? $resp2['result']['photos'][$j]['width'] : "";
            array_push($photo_width_arr, $photo_width);
            $photo_reference = isset($resp2['result']['photos'][$j]['photo_reference']) ? $resp2['result']['photos'][$j]['photo_reference'] : "";
            array_push($photo_reference_arr, $photo_reference);
        }
    }
    $exist = true;
    for($i=0; $i<5; $i++) {
        if ($photo_reference_arr[$i] == ""){
            $exist = false;
            break;
        }
        $maxwidth = urlencode($photo_width_arr[$i]);
        $photoreference = urlencode($photo_reference_arr[$i]);
        $photo_url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=$maxwidth&photoreference=$photoreference&key=AIzaSyCAvB0UHhavDE5H4j1eBotggoeBV69Z0Jo";
        $photo = file_get_contents($photo_url);
        $filename = "img" . $i . ".png";
        $img = "/home/scf-05/zhuoyiny/apache2/htdocs/imgs/$filename";
        file_put_contents($img, $photo);
    }

    array_push(
                $data_arr1,
                    $author_name_arr,
                    $profile_photo_url_arr,
                    $text_arr,
                    $exist
                );
echo json_encode($data_arr1);
exit();

}

?>







<?php

function geocode($keyword,$category,$radius,$lati,$longi,$address){
    $formatted_address="";
    if($lati=="" && $longi==""){
        $address = urlencode($address);
        $url = "https://maps.googleapis.com/maps/api/geocode/json?address={$address}&key=AIzaSyB1iP7hvJJ7ISTIhh2UzEiV5Jy5bnbyrf4";
        $resp_json = file_get_contents($url);
        $resp = json_decode($resp_json, true);
        if($resp['status']=='OK'){

            $lati = isset($resp['results'][0]['geometry']['location']['lat']) ? $resp['results'][0]['geometry']['location']['lat'] : "";
            $longi = isset($resp['results'][0]['geometry']['location']['lng']) ? $resp['results'][0]['geometry']['location']['lng'] : "";
            $formatted_address = isset($resp['results'][0]['formatted_address']) ? $resp['results'][0]['formatted_address'] : "";
        }
        else{
            echo "<strong>ERROR: {$resp['status']}</strong>";
            return false;
        }
    }

        $keyword = urlencode($keyword);
        $category = urlencode($category);
        $radius = urlencode($radius*1609);
        $lati = urlencode($lati);
        $longi = urlencode($longi);

        $url1 ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$lati,$longi&radius=$radius&type=$category&keyword=$keyword&key=AIzaSyB1iP7hvJJ7ISTIhh2UzEiV5Jy5bnbyrf4";
        $resp_json1 = file_get_contents($url1);
        $resp1 = json_decode($resp_json1, true);

        $icon_arr = array();
        $name_arr = array();
        $vicinity_arr = array();
        $placeid_arr = array();
        $latdes_arr = array();
        $londes_arr = array();

        if($resp1['status']=='OK'){
            $len = count($resp1['results']);
            for($i = 0; $i<$len; $i++) {
                $latdes = isset($resp1['results'][$i]['geometry']['location']['lat']) ? $resp1['results'][$i]['geometry']['location']['lat'] : "";
                array_push($latdes_arr, $latdes);
                $londes = isset($resp1['results'][$i]['geometry']['location']['lng']) ? $resp1['results'][$i]['geometry']['location']['lng'] : "";
                array_push($londes_arr, $londes);
                $icon = isset($resp1['results'][$i]['icon']) ? $resp1['results'][$i]['icon'] : "";
                array_push($icon_arr, $icon);
                $name = isset($resp1['results'][$i]['name']) ? $resp1['results'][$i]['name'] : "";
                array_push($name_arr, $name);
                $vicinity = isset($resp1['results'][$i]['vicinity']) ? $resp1['results'][$i]['vicinity'] : "";
                array_push($vicinity_arr, $vicinity);
                $placeid = isset($resp1['results'][$i]['place_id']) ? $resp1['results'][$i]['place_id'] : "";
                array_push($placeid_arr, $placeid);
            }



        if($lati && $longi ){

           $data_arr = array();

                array_push(
                    $data_arr,
                        $lati,
                        $longi,
                        $formatted_address,
                        $icon_arr,
                        $name_arr,
                        $vicinity_arr,
                        $placeid_arr,
                        $latdes_arr,
                        $londes_arr
                    );

                return $data_arr;
            }else{
                return false;
            }

        }

    }




if(isset($_POST["keyword"])){

    $data_arr = geocode($_POST['keyword'], $_POST['category'], $_POST['radius'], $_POST['lat_js'], $_POST['lon_js'], $_POST['address']);
    echo json_encode($data_arr);
    exit(0);
}

?>






<!DOCTYPE html>
<html>

<head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>zhuoying_HW6</title>



    <style>

        body {
            font-family: times,"Times New Roman","serif";
        }

        #imgCSS {
            width:600px;
            padding:10px;
            border: 2px solid rgb(200,200,200);
        }

        #floating_panel {
            position: absolute;
            display: none;
            z-index: 5;
            background-color: rgb(240,240,240);
            padding: 0;
            border: 1px solid *#999*;
            text-align: center;
            width: 90px;
        }
        #walk:hover, #bicycle:hover, #drive:hover{
            background-color: rgb(200,200,200);
        }

        #walk, #bicycle, #drive {
            padding: 6px 4px;
            margin:0;
        }

        .search_box {
            text-align: left;
            margin: 0px auto;
            width: 600px;
            background-color: rgb(250,250,250);
            border-style: solid;
            border-width: 3px;
            border-color: #C7C7C7;
        }
        form h1 {
            font-family: times;
            text-align: center;
            margin: 0;
            padding: 0;
        }

        #myForm {
            padding: 0 8px;
        }
        .resultTable table, .resultTable th, .resultTable td{
            border: 2px solid rgb(190,190,190);
            border-collapse: collapse;
        }

        .reviewTable table, .reviewTable th, .reviewTable td{
            width: 65%;
            border: 2px solid rgb(190,190,190);
            border-collapse: collapse;
        }
        table {
            margin: 20px auto;
            width: 85%;
        }
        #noRec {
            text-align: center;
            background-color: rgb(250,250,250);
            width: 65%;
            border: 2px solid rgb(190,190,190);
        }
        #map {
            position: absolute;
            z-index: 1;
            height: 280px;
            width: 35%;
            display: none;
        }
        .floatLeft {
            float: left;
        }


    </style>
</head>

<body>

    <div class="search_box">
        <form id="myForm" >
            <h1><i>Travel and Entertainment Search</i></h1>
            <hr>
            <b>Keyword</b>
            <input type="text" name="keyword" id="keyword" value="" required="">
            <br>
            <b>Category</b>
            <select required="" name="category" id="category" value="">
                <option value="default">defaults</option>
                <option value="cafe">cafe</option>
                <option value="bakery">bakery</option>
                <option value="restaurant">restaurant</option>
                <option value="beauty salon">beauty salon</option>
                <option value="casino">casino</option>
                <option value="movie theater">movie theater</option>
                <option value="lodging">lodging</option>
                <option value="airport">airport</option>
                <option value="train station">train station</option>
                <option value="subway station">subway station</option>
                <option value="bus station">bus station</option>
            </select>
            <br>
            <b class="floatLeft">Distance(miles)</b>
            <input class="floatLeft" type="text" name="radius" id="radius" required="" value="10" placeholder="10">
            <b class="floatLeft">from</b>
            <div class="floatLeft">
                <input type="radio" name="location" value="" checked="true" id="radio1"> Here <br>
                <input type="radio" name="location" value="" id="radio2">
                <input type="text" name="address" value="" id= "loc" placeholder="location" disabled><br>
            </div><br><br><br>
            <button type="submit" value="Submit" id="submit" style="margin-left: 60px">Search</button>
            <button value="" id="reset" style="margin-left: 0 0 30px 10px">Clear</button>
            <br><br>

        </form>
    </div>
    <p id="resultTable1"></p>
    <p id="name_show"></p>

    <div id="btn1">
        <p style="text-align:center" id="show_reviews"></p>
        <p style="text-align:center" id="arrow1_show"></p>
    </div>


    <p id="reviewTable"></p>

    <div id="btn2">
        <p style="text-align:center" id="show_photos"></p>
        <p style="text-align:center" id="arrow2_show"></p>
    </div>

    <p id="imageTable"></p>



    <div id="floating_panel">
        <p id="walk" value="WALKING">Walk there</p>
        <p id="bicycle" value="BICYCLING">Bike there</p>
        <p id="drive" value="DRIVING">Drive there</p>
    </div>

    <div id="map"></div>






<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCAvB0UHhavDE5H4j1eBotggoeBV69Z0Jo">
    </script>


<script type="text/javascript">
    var lat_js;
    var lon_js;

    window.onload = function(){
        document.getElementById("submit").disabled = true;
        try{
            var xmlHttp = new XMLHttpRequest();
            xmlHttp.open("GET",'http://ip-api.com/json',false);
            xmlHttp.send();
            var json = JSON.parse(xmlHttp.responseText);
        }catch(e){
            alert("Get geolocation failed!");
            return;
        }
        lat_here = json.lat;
        lon_here = json.lon;

        document.getElementById("submit").disabled = false;
    };

    document.getElementById("radio1").addEventListener("click", function(){
        if(document.getElementById("radio1").checked == true) {
            document.getElementById("loc").disabled = true;
        }
    });
    document.getElementById("radio2").addEventListener("click", function(){
        if(document.getElementById("radio2").checked == true) {
            document.getElementById("loc").disabled = false;
            document.getElementById("loc").required = true;
        }
    });

    document.getElementById("reset").addEventListener("click", function(e){
        document.getElementById("keyword").value = '';
        document.getElementById("radius").value = '';
        document.getElementById("loc").value = '';
        document.getElementById("category").value = 'default';
        document.getElementById("radio1").checked = true;
        document.getElementById("loc").required = false;

        document.getElementById("resultTable1").style.display = "none";
        document.getElementById("name_show").style.display = "none";
        document.getElementById("imageTable").style.display = "none";
        document.getElementById("reviewTable").style.display = "none";
        document.getElementById("show_reviews").style.display = "none";
        document.getElementById("show_photos").style.display = "none";
        document.getElementById("arrow1_show").style.display = "none";
        document.getElementById("arrow2_show").style.display = "none";
        document.getElementById("floating_panel").style.display = "none";
        document.getElementById("map").style.display = "none";

        if(e.preventDefault){
            e.preventDefault();
        }
        e.defaultPrevented = true;
        return false;
    });


    var arr;
    document.getElementById("myForm").addEventListener("submit", function(e){

        e.preventDefault();
        var keyword = document.getElementById("myForm").keyword.value;
        var category = document.getElementById("myForm").category.value;
        var radius = document.getElementById("myForm").radius.value;
        var location;
        var address;

        if(document.getElementById("radio1").checked == true) {
            lat_js = lat_here;
            lon_js = lon_here;
            address = "";
        }
        if(document.getElementById("radio2").checked == true) {
            lat_js = "";
            lon_js = "";
            address = document.getElementById("myForm").address.value;
        }
        var data= "&keyword="+keyword+"&category="+category+"&radius="+radius+"&lat_js="+lat_js+"&lon_js="+lon_js+"&address="+address;
        var xhr = new XMLHttpRequest();
        xhr.open('post', 'place.php',true );
        xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhr.send(data);
        xhr.onreadystatechange = function () {

            if (xhr.readyState == 4 && xhr.status == 200) {
                // console.log(xhr.responseText);
                var ar = JSON.parse(xhr.responseText);
                arr = ar;
                // console.log(ar);
            }
            if (typeof ar != "undefined") {
                generateFirstTable(ar);
            }
        }
    });



    function generateFirstTable(ar){

        var html_text;
        html_text = "<html><body>";
        html_text += "<div class=resultTable>";
        html_text += "<table>";
        html_text += "<tbody>";

        if(ar==null){
            html_text += "<tr><td id=noRec>"+"No records have been found"+"</td></tr>";
        }else{
            html_text += "<tr>";
            html_text += "<th>Category</th>";
            html_text += "<th>Name</th>";
            html_text += "<th>address</th>";
            html_text += "</tr>";

            for (i = 0; i < ar[3].length; i++) {
                html_text += "<tr>";
                html_text += "<td><img src="+ ar[3][i] +" height=40 width=50></td>";
                html_text += '<td id="'+i+'" onclick="nameShow(this.id);reply_click(this.id);buttonShow();">' + ar[4][i] + "</td>";
                html_text += '<td id="str'+i+'" onclick="showMap(this);">' + ar[5][i] + "</td>";
                html_text += "</tr>";
            }
        }
        html_text += "</tbody>";
        html_text += "</table></div>";
        html_text += "</body></html>";
        document.getElementById("resultTable1").innerHTML=html_text;
        document.getElementById("resultTable1").style.display = "block";
        document.getElementById("imageTable").style.display = "none";
        document.getElementById("reviewTable").style.display = "none";
        document.getElementById("name_show").style.display = "none";
        document.getElementById("show_reviews").style.display = "none";
        document.getElementById("show_photos").style.display = "none";
        document.getElementById("arrow1_show").style.display = "none";
        document.getElementById("arrow2_show").style.display = "none";
        document.getElementById("floating_panel").style.display = "none";
        document.getElementById("map").style.display = "none";
    }



        nameShow = function(clicked_id){
            var html_text = '<h3 style="text-align:center">'+arr[4][clicked_id]+"</h3>";
            document.getElementById("name_show").innerHTML=html_text;
            document.getElementById("name_show").style.display = "block";
        };


        reply_click = function(clicked_id){

            document.getElementById("resultTable1").style.display = "none";
            document.getElementById("floating_panel").style.display = "none";
            document.getElementById("map").style.display = "none";
            var place_id = arr[6][clicked_id];
            var xhr = new XMLHttpRequest();
            xhr.open('get', 'place.php?key='+place_id,true );
            xhr.send();
            xhr.onreadystatechange = function () {

                if (xhr.readyState == 4 && xhr.status == 200) {
                    // console.log(xhr.responseText);
                    var data_arr1 = JSON.parse(xhr.responseText);
                    // console.log(data_arr1);
                }
                if (typeof data_arr1 != "undefined") {
                    generateReviewTable(data_arr1);
                    generatePhotoTable(data_arr1);
                }
            }
        };


        function generatePhotoTable(data_arr1){
            var html_text;
            html_text = "<html><body>";
            html_text += "<div>";
            html_text += "<table>";
            html_text += "<tbody>";
            if(data_arr1[3]==false){
                html_text += "<tr><td id=noRec>"+"No Photos Found"+"</td></tr>";
            }else{
                for (i = 0; i < 5; i++) {
                    var ImgObj = new Image();
                    ImgObj.src = "imgs/img"+i+".png";

                    if(ImgObj.fileSize > 0 || (ImgObj.width > 0 && ImgObj.height > 0)){
                        html_text += '<tr>';
                        html_text += '<td><div style="text-align:center"><a href="imgs/img'+i+'.png" target="_blank"><img id="imgCSS" src="imgs/img'+i+'.png?v="'+Math.random()+' "width:600px;padding:10px;border: 2px solid rgb(200,200,200);"></a></div></td>';
                        html_text += "</tr>";
                    }
                }
            }
            html_text += "</tbody>";
            html_text += "</table></div>";
            html_text += "</body></html>";
            document.getElementById("imageTable").innerHTML=html_text;
        }



        function generateReviewTable(data_arr1){
            var html_text;
            html_text = "<html><body>";
            html_text += "<div class=reviewTable>";
            html_text += "<table>";
            html_text += "<tbody>";
            var num = 0;
            for(i=0; i<data_arr1[2].length; i++) {
                if(data_arr1[2][i] == ""){
                    num++;
                }else{
                    break;
                }
            }

            if(num == data_arr1[2].length){
                html_text += "<tr><td id=noRec>"+"No Reviews Found"+"</td></tr>";
            }else{
                for (i = 0; i < 5; i++) {
                    html_text += "<tr>";
                    html_text += '<th><img src="'+ data_arr1[1][i] + '" alt="" height=30 width=30>' + data_arr1[0][i]+"</th>";
                    html_text += "</tr><tr>";
                    html_text += "<td>" + data_arr1[2][i] + "</td>";
                    html_text += "</tr>";
                }
            }
            html_text += "</tbody>";
            html_text += "</table></div>";
            html_text += "</body></html>";
            document.getElementById("reviewTable").innerHTML=html_text;
        }



        buttonShow = function(){
            var reviews = "click to show reviews";
            var arrow1 = '<img src="arrow_down.png" height=20 width=30 >';
            var photos = "click to show photos";
            var arrow2 = '<img src="arrow_down.png" height=20 width=30>';
            document.getElementById("show_reviews").innerHTML=reviews;
            document.getElementById("arrow1_show").innerHTML=arrow1;
            document.getElementById("show_photos").innerHTML=photos;
            document.getElementById("arrow2_show").innerHTML=arrow2;
            document.getElementById("show_reviews").style.display = "block";
            document.getElementById("arrow1_show").style.display = "block";
            document.getElementById("show_photos").style.display = "block";
            document.getElementById("arrow2_show").style.display = "block";
        };

        document.getElementById("btn1").addEventListener("click",function(event){
            if (document.getElementById("show_reviews").innerHTML=="click to show reviews") {
                document.getElementById("arrow1_show").innerHTML='<img src="arrow_up.png" height=20 width=30 >';
                document.getElementById("show_reviews").innerHTML = "click to hide reviews";
                document.getElementById("reviewTable").style.display = "block";
                document.getElementById("imageTable").style.display = "none";
                document.getElementById("arrow2_show").innerHTML ='<img src="arrow_down.png" height=20 width=30 >';
                document.getElementById("show_photos").innerHTML ="click to show photos";
            }
            else {
                document.getElementById("arrow1_show").innerHTML ='<img src="arrow_down.png" height=20 width=30 >';
                document.getElementById("show_reviews").innerHTML ="click to show reviews";
                document.getElementById("reviewTable").style.display = "none";
            }
        });

        document.getElementById("btn2").addEventListener("click",function(event){
            if (document.getElementById("show_photos").innerHTML=="click to show photos") {
                document.getElementById("arrow2_show").innerHTML='<img src="arrow_up.png" height=20 width=30 >';
                document.getElementById("show_photos").innerHTML = "click to hide photos";
                document.getElementById("imageTable").style.display = "block";
                document.getElementById("reviewTable").style.display = "none";
                document.getElementById("arrow1_show").innerHTML ='<img src="arrow_down.png" height=20 width=30 >';
                document.getElementById("show_reviews").innerHTML ="click to show reviews";
            }
            else {
                document.getElementById("arrow2_show").innerHTML ='<img src="arrow_down.png" height=20 width=30 >';
                document.getElementById("show_photos").innerHTML ="click to show photos";
                document.getElementById("imageTable").style.display = "none";
            }
        });




        showMap = function(elm){

            clicked_id = elm.id;
            id1 = parseInt(clicked_id.substring(3));
            var des_lat = arr[7][id1];
            var des_lon = arr[8][id1];
            var rect = elm.getBoundingClientRect();
            if (map.style.display == "block") {
                map.style.display = "none";
                floating_panel.style.display = "none";
            } else {
                map.style.display = "block";
                floating_panel.style.display = "block";
                map.style.top = rect.bottom + window.scrollY + "px";
                floating_panel.style.top = rect.bottom + window.scrollY + "px";
                map.style.left = rect.left + "px";
                floating_panel.style.left = rect.left + "px";
            }
            initMap(des_lat, des_lon);
        };

        function initMap(des_lat, des_lon) {

            var directionsDisplay = new google.maps.DirectionsRenderer;
            var directionsService = new google.maps.DirectionsService;
            var map = new google.maps.Map(document.getElementById('map'), {
              zoom: 14,
              center: new google.maps.LatLng(parseFloat(des_lat), parseFloat(des_lon))
            });
            var marker = new google.maps.Marker({
              position: new google.maps.LatLng(parseFloat(des_lat), parseFloat(des_lon)),
              visible: true,
              map: map
            });

            directionsDisplay.setMap(map);

            document.getElementById("drive").addEventListener("click",function(){
                var selectedMode = document.getElementById("drive").getAttribute("value");
                calculateAndDisplayRoute(directionsService, directionsDisplay,des_lat, des_lon, marker, selectedMode);
            });
            document.getElementById("walk").addEventListener("click",function(){
                var selectedMode = document.getElementById("walk").getAttribute("value");
                calculateAndDisplayRoute(directionsService, directionsDisplay,des_lat, des_lon, marker, selectedMode);
            });
            document.getElementById("bicycle").addEventListener("click",function(){
                var selectedMode = document.getElementById("bicycle").getAttribute("value");
                calculateAndDisplayRoute(directionsService, directionsDisplay,des_lat, des_lon, marker, selectedMode);
            });


      function calculateAndDisplayRoute(directionsService, directionsDisplay,des_lat, des_lon, marker, selectedMode) {
        var lat_org;
        var lon_org;
        try{
            var xmlHttp = new XMLHttpRequest();
            xmlHttp.open("GET",'http://ip-api.com/json',false );
            xmlHttp.send();
            var json = JSON.parse(xmlHttp.responseText);
        }catch(e){
            alert("Get geolocation failed!");
            return;
        }
        if(document.getElementById("radio1").checked == true) {
            lat_org = json.lat;
            lon_org = json.lon;
        }
        if(document.getElementById("radio2").checked == true) {
            lat_org = parseFloat(arr[0]);
            lon_org = parseFloat(arr[1]);
        }
        marker.visible = false;
        directionsService.route({
          origin: {lat: lat_org, lng: lon_org},
          destination: {lat: parseFloat(des_lat), lng: parseFloat(des_lon)},
          travelMode: google.maps.TravelMode[selectedMode]
        }, function(response, status) {
          if (status == 'OK') {
            directionsDisplay.setDirections(response);
          } else {
            window.alert('Directions request failed due to ' + status);
          }
        });
      }

    }


</script>


</body>

</html>
