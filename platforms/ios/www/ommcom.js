
if(navigator.onLine == false){
  alert("No Internet Connection Found !");

}
////fetching Menu//////////////////////
function getmenu(){
  var menu='';
  $.ajax({
        url: "https://ommcomnews.com/api/v0.1/categories"
    }).then(function(data) {
      menu='<div class="menuClose"><a href="javascript:"><i class="fa fa-times"></i></a></div><ul>';
      $.each(data, function(index, value) {
          menu=menu + "<li>" + "<a href='videos.html?slug="+value.slug+"'>" + value.name + "</a></li>";
        });
        menu = menu + "</ul>";
        $('.menu').html(menu);
    });

}
////Fetching Breaking News////
function breakingnews(){
  var news='';
  var j=0;
  $.ajax({
        url: "https://ommcomnews.com/api/v0.1/breakingNews"
    }).then(function(data) {
      $.each(data, function(index, value) {
                for (var i = 0; i < value.length; i++) {
                  j++;
                news=news + '<li class="news-item" id="item'+j+'" style="display:none;">' + value[i].title+'</li>';
                }

        });
$('.breakingnewsstrip').append(news);
    });
}
////Featured News////////////
function featurednews(){
  var name='';
  var featured_image='';
  var short_description='';
  var slug='';
  var topnewsnow='';

  $.ajax({
        url: "https://ommcomnews.com/api/v0.1/postsHome"
    }).then(function(data) {
      name=data.FEATUREDNEWS.name;

      if(data.FEATUREDNEWS.featured_image != '' && data.FEATUREDNEWS.featured_image != null){
        featured_image='https://ommcomnews.com/file/news/exact_560_360/' + data.FEATUREDNEWS.featured_image;
      }else{

        temp_image=data.FEATUREDNEWS.file_path;
        temp_image=temp_image.replace('.mp4','.jpg');
        featured_image='https://ommcomnews.com/file/news/exact_560_360/' + temp_image;
      }

      short_description=data.FEATUREDNEWS.short_description;
      slug=data.FEATUREDNEWS.slug;
    //  console.log(data);
      $('#fdate').append(findduration(data.FEATUREDNEWS.approved_date));
      $('#featured_news').append("<a href='newsdetail.html?slug="+ slug +"' style='text-decoration:none;color:white'>" + name + "</a>");
      document.getElementById('featured_news_image').src=featured_image;
//console.log(featured_image);
//Fetching top news
//console.log(data.TOPNEWSNOW);
var topimage1 = '';

if(data.TOPNEWSNOW[0].is_video == 1){
  topimage1 = data.TOPNEWSNOW[0].file_path;
  topimage1 = topimage1.replace(".mp4", ".jpg");
}else{
  topimage1 = data.TOPNEWSNOW[0].featured_image;
//  topimage1 = topimage1.replace(".mp4", ".jpg");
}


var topimage2 = '';
if(data.TOPNEWSNOW[1].is_video == 1){
  topimage2 = data.TOPNEWSNOW[1].file_path;
  topimage2 = topimage2.replace(".mp4", ".jpg");
}else{
  topimage2 = data.TOPNEWSNOW[1].featured_image;
//  topimage2 = topimage2.replace(".mp4", ".jpg");
}

var topimage3 = '';
if(data.TOPNEWSNOW[2].is_video == 1){
  topimage3 = data.TOPNEWSNOW[2].file_path;
  topimage3 = topimage3.replace(".mp4", ".jpg");
}else{
  topimage3 = data.TOPNEWSNOW[2].featured_image;
  //topimage3 = topimage3.replace(".mp4", ".jpg");
}

document.getElementById('top1').src='https://ommcomnews.com/file/news/exact_285_170/' + topimage1;
document.getElementById('top2').src='https://ommcomnews.com/file/news/exact_285_170/' + topimage2;
document.getElementById('top3').src='https://ommcomnews.com/file/news/exact_285_170/' + topimage3;

//Citizen Journalist
var citizen_image=data.CITIZEN_CUSTOMIZE.file_path;
var citizen_name=data.CITIZEN_CUSTOMIZE.name;
$('#citizen_name').html(citizen_name);
document.getElementById('citizen_image').src='https://ommcomnews.com/file/citizenCustomize/original/' + citizen_image;

//Category News
var category_news=data.CATEGORY_NEWS;
var cat1_title=category_news[0].categoryname;
var cat1_desc=category_news[0].name;
var cat1_image=category_news[0].featured_image;

$('#cat1_title').append(cat1_title);
$('#cat1_desc').append("<a style='color:white;text-decoration:none' href='newsdetail.html?slug=" + category_news[0]['slug'] + "'>" + cat1_desc + "</a>");
$('#cat1time').append(findduration(category_news[0].approved_date));
document.getElementById('cat1_image').src='https://ommcomnews.com/file/news/exact_360_180/'+cat1_image;

var cat2_title=category_news[1].categoryname;
var cat2_desc=category_news[1].name;
var cat2_image=category_news[1].featured_image;
$('#cat2_title').append(cat2_title);
$('#cat2_desc').append("<a style='color:white;text-decoration:none' href='newsdetail.html?slug=" + category_news[1]['slug'] + "'>" + cat2_desc + "</a>");
$('#cat2time').append(findduration(category_news[1].approved_date));
document.getElementById('cat2_image').src='https://ommcomnews.com/file/news/exact_360_180/'+cat2_image;

var cat3_title=category_news[2].categoryname;
var cat3_desc=category_news[2].name;
var cat3_image=category_news[2].featured_image;

$('#cat3_title').append(cat3_title);
$('#cat3_desc').append("<a style='color:white;text-decoration:none' href='newsdetail.html?slug=" + category_news[2]['slug'] + "'>" + cat3_desc + "</a>");
$('#cat3time').append(findduration(category_news[2].approved_date));
document.getElementById('cat3_image').src='https://ommcomnews.com/file/news/exact_360_180/'+cat3_image;

var cat4_title=category_news[3].categoryname;
var cat4_desc=category_news[3].name;
var cat4_image=category_news[3].featured_image;

$('#cat4_title').append(cat4_title);
$('#cat4_desc').append("<a style='color:white;text-decoration:none' href='newsdetail.html?slug=" + category_news[3]['slug'] + "'>" + cat4_desc + "</a>");
$('#cat4time').append(findduration(category_news[3].approved_date));
document.getElementById('cat4_image').src='https://ommcomnews.com/file/news/exact_360_180/'+cat4_image;

var cat5_title=category_news[4].categoryname;
var cat5_desc=category_news[4].name;
var cat5_image=category_news[4].featured_image;

$('#cat5_title').append(cat5_title);
$('#cat5_desc').append("<a style='color:white;text-decoration:none' href='newsdetail.html?slug=" + category_news[4]['slug'] + "'>" + cat5_desc + "</a>");
$('#cat5time').append(findduration(category_news[4].approved_date));
document.getElementById('cat5_image').src='https://ommcomnews.com/file/news/exact_360_180/'+cat5_image;

var cat6_title=category_news[5].categoryname;
var cat6_desc=category_news[5].name;
var cat6_image=category_news[5].featured_image;

$('#cat6_title').append(cat6_title);
$('#cat6_desc').append("<a style='color:white;text-decoration:none' href='newsdetail.html?slug=" + category_news[5]['slug'] + "'>" + cat6_desc + "</a>");
$('#cat6time').append(findduration(category_news[5].approved_date));
document.getElementById('cat6_image').src='https://ommcomnews.com/file/news/exact_360_180/'+cat6_image;

var viral_video=data.VIRAL_VIDEO;
$('#viral_video').append(viral_video.name);
var viral_image=viral_video.file_path;
var viral_image_v=viral_image;
viral_image=viral_image.replace('.mp4','.jpg');
//document.getElementById('viral_image').src='http://ommcomnews.com/public/file/news/original/'+viral_image;
$('#viral_video_frame').attr('poster','https://ommcomnews.com/file/news/original/'+viral_image);
var video2 = document.getElementById('viral_video_frame');
var source2 = document.createElement('source');
source2.setAttribute('src','https://ommcomnews.com/file/news/original/'+viral_image_v);
video2.appendChild(source2);

var top_video=data.TOP_VIDEO;
$('#top_video').html(top_video.name);
var top_image=top_video.file_path;
var top_image_v=top_image;
top_image=top_image.replace('.mp4','.jpg');
$('#top_video_frame').attr('poster','https://ommcomnews.com/file/news/original/'+top_image);
var video1 = document.getElementById('top_video_frame');
var source1 = document.createElement('source');
//source1.setAttribute('src','http://ommcomnews.com/public/file/news/original/'+top_image_v);
//video1.appendChild(source1);

var advertisement='';
advertisement=data.ADVERTISEMENT;
adv_top=advertisement[0].file_path;
adv_middle=advertisement[1].file_path;
adv_footer=advertisement[2].file_path;
document.getElementById('adv_top').innerHTML="<img  src=" + 'https://ommcomnews.com/file/advertisement/original/' + adv_top + "  />";
document.getElementById('adv_middle').innerHTML="<img  src=" + 'https://ommcomnews.com/file/advertisement/original/' + adv_middle + "  />";
document.getElementById('adv_footer').innerHTML="<img  src=" + 'https://ommcomnews.com/file/advertisement/original/' + adv_footer + "  />";
//document.getElementById('adv_footer').src='http://ommcomnews.com/public/file/news/original/'+adv_footer;
//console.log(advertisement);

    });
}

function gettopnews(){ //Fetch top 5 news
  var topnews='';
  var top1,top2,top3,top4,top5;

  $.ajax({
        url: "http://ommcomnews.com/api/v0.1/posts/topNewsList"
    }).then(function(data) {
    //  console.log(data);
      top1=data.data[0];
      $('#top1_title').append("<a id='top1_href' style='text-decoration:none;font-size:15px'>" + top1.name.substring(0,25) + "....</a>");
      $('#top1_href').attr('href','newsdetail.html?slug='+top1.slug);
      $('#appdate1').append(findduration(top1.approved_date));
      if(top1.is_video == 0){
        $('#top1_media').html("<img src=" + 'https://ommcomnews.com/file/news/exact_375_200/'+top1.featured_image + " />");
      }else{
        var poster1=top1.file_path;
        poster1=poster1.replace('.mp4','.jpg');
        poster1 = 'https://ommcomnews.com/file/news/exact_375_200/'+ poster1;
      //  $('#top1_media').html("<video id='top1_media_video' height='50px' controls poster=" + poster1 + "></video>");
      // var tv1 = document.getElementById('top1_media_video');
      //  var ts1 = document.createElement('source');
      //  ts1.setAttribute('src','http://ommcomnews.com/public/file/news/original/'+ top1.file_path);
      //  tv1.appendChild(ts1);
      $('#top1_media').html("<a href='newsdetail.html?slug="+top1.slug+"'><img src=" + poster1  + " /></a>");
      }
    top2=data.data[1];
      $('#top2_title').append("<a id='top2_href' style='text-decoration:none;font-size:15px'>" + top2.name.substring(0,25) + "....</label>");
    $('#top2_href').attr('href','newsdetail.html?slug='+top2.slug);
    $('#appdate2').append(findduration(top2.approved_date));
    if(top2.is_video == 0){
        $('#top2_media').html("<img src=" + 'https://ommcomnews.com/file/news/exact_375_200/'+top2.featured_image + " />");
    }else{
      var poster2=top2.file_path;
      poster2=poster2.replace('.mp4','.jpg');
      poster2 = 'https://ommcomnews.com/file/news/exact_375_200/'+ poster2;
    //  $('#top2_media').html("<video id='top2_media_video' width='100%'  controls poster=" + poster2 + "></video>");
    //  var tv2 = document.getElementById('top2_media_video');
    //  var ts2 = document.createElement('source');
    //  ts2.setAttribute('src','http://ommcomnews.com/public/file/news/original/'+ top2.file_path);
    //  tv2.appendChild(ts2);
  $('#top2_media').html("<a href='newsdetail.html?slug="+top2.slug+"'><img src=" + poster2  + " /></a>");
    }

    top3=data.data[2];
    $('#top3_title').append("<a id='top3_href' style='text-decoration:none;font-size:15px'>" + top3.name.substring(0,25) + "....</label>");
    $('#top3_href').attr('href','newsdetail.html?slug='+top3.slug);
    $('#appdate3').append(findduration(top3.approved_date));
    if(top3.is_video == 0){
        $('#top3_media').html("<img src=" + 'https://ommcomnews.com/file/news/exact_375_200/'+top3.featured_image + " />");
    }else{
      var poster3=top3.file_path;
      poster3=poster3.replace('.mp4','.jpg');
      poster3 = 'https://ommcomnews.com/file/news/exact_375_200/'+ poster3;
    //  $('#top3_media').html("<video id='top3_media_video' width='100%'  controls poster=" + poster3 + "></video>");
    //  var tv3 = document.getElementById('top3_media_video');
    //  var ts3 = document.createElement('source');
    //  ts3.setAttribute('src','http://ommcomnews.com/public/file/news/original/'+ top3.file_path);
    //  tv3.appendChild(ts3);
      $('#top3_media').html("<a href='newsdetail.html?slug="+top3.slug+"'><img src=" + poster3  + " /></a>");
    }

    top4=data.data[3];
  $('#top4_title').append("<a id='top4_href' style='text-decoration:none;font-size:15px'>" + top4.name.substring(0,25) + "....</label>");
    $('#top4_href').attr('href','newsdetail.html?slug='+top4.slug);
    $('#appdate4').append(findduration(top4.approved_date));
    if(top4.is_video == 0){
        $('#top4_media').html("<img src=" + 'httpss://ommcomnews.com/file/news/exact_375_200/'+top4.featured_image + " />");
    }else{
      var poster4=top4.file_path;
      poster4=poster4.replace('.mp4','.jpg');
      poster4 = 'https://ommcomnews.com/file/news/exact_375_200/'+ poster4;
    //  $('#top4_media').html("<video id='top4_media_video' width='100%'  controls poster=" + poster4 + "></video>");
    //  var tv4 = document.getElementById('top4_media_video');
    //  var ts4 = document.createElement('source');
    //  ts4.setAttribute('src','http://ommcomnews.com/public/file/news/original/'+ top4.file_path);
    //  tv4.appendChild(ts4);
      $('#top4_media').html("<a href='newsdetail.html?slug="+top4.slug+"'><img src=" + poster4  + " /></a>");
    }

    top5=data.data[4];
    $('#top5_title').append("<a id='top5_href' style='text-decoration:none;font-size:15px'>" + top5.name.substring(0,25) + "....</label>");
    $('#top5_href').attr('href','newsdetail.html?slug='+top5.slug);
    $('#appdate5').append(findduration(top5.approved_date));
    if(top5.is_video == 0){
        $('#top5_media').html("<img src=" + 'https://ommcomnews.com/file/news/exact_375_200/'+top5.featured_image + " />");
    }else{
      var poster5=top5.file_path;
      poster5=poster5.replace('.mp4','.jpg');
      poster5 = 'https://ommcomnews.com/file/news/exact_375_200/'+ poster5;
      //$('#top5_media').html("<video id='top5_media_video' width='100%'  controls poster=" + poster5 + "></video>");
    //  var tv5 = document.getElementById('top5_media_video');
    //  var ts5 = document.createElement('source');
    //  ts5.setAttribute('src','http://ommcomnews.com/public/file/news/original/'+ top5.file_path);
    //  tv5.appendChild(ts5);
      $('#top5_media').html("<a href='newsdetail.html?slug="+top5.slug+"'><img src=" + poster5  + " /></a>");
    }

    });
}
////////////////////////////
function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}
////Get News Detail///////////////////////////
function getnewsdetail(){
  var slug = getParameterByName('slug');
  $.ajax({
        url: "https://ommcomnews.com/api/v0.1/posts/slug/" + slug
    }).then(function(data) {
    var news_name=data.News.name;

    var nc=data.NewsComment;
    var cc='';
    for(var ii=0;ii<nc.length;ii++){
      //  console.log(data);
cc=nc[ii].comment+" ...... By : <strong>"+nc[ii].name+"</strong>";
        $('#newscomment').append(cc + "<br><hr>");
cc='';
    }
    $('#news_name').append(news_name);
    var news_desc=data.News.long_description;
document.getElementById("id").value=data.News.id;
//news_desc=news_desc.replace(/\\n\\n/g, "</p><p>");
//news_desc=news_desc.replace(/\\n/g, "<br/>");
news_desc=news_desc.replace(/\r\n\r\n/g,'<p>');
//news_desc=news_desc.replace(/\r\n/g,'<br>');
    var sp= news_desc.replace("**","");
    sp= sp.replace("**:",":");
    sp= sp.replace("**",'');
   news_desc=sp;
     //alert(news_desc);
    $('#news_desc').html(news_desc);
    var str='';
    var news_image=data.News.featured_image;
    var news_video='';
    var news_poster='';
    if(data.News.is_video == 1){
      news_image=data.News.file_path;
      news_video=news_image;
      news_image=news_image.replace('.mp4','.jpg');
      news_poster='https://ommcomnews.com/file/news/exact_360_180/'+ news_image;
    }
    news_image='https://ommcomnews.com/file/news/exact_360_180/'+ news_image;
    str=news_image;
  //  console.log(data.News);
$('#ndtime').append(findduration(data.News.approved_date));
$('#ndtime').append(",&nbsp;&nbsp&nbsp;<em>&nbsp;"+data.News.journalist_name+"</em>");
$('#ndtime').append("&nbsp;&nbsp;&nbsp;<i class='fa fa-eye fa-fw'></i> "+data.News.news_count);
if(data.News.is_video != 1){
  document.getElementById("news_image").src=str;
}else{
  $('.swiper-slide').html("<video id='news_video' width='100%' height='230px'  controls  poster=" + news_poster + "></video>");
  var newsv = document.getElementById('news_video');
  var newss = document.createElement('source');
  newss.setAttribute('src','https://ommcomnews.com/file/news/original/'+ news_video);
  newsv.appendChild(newss);
}
var videos=data.News.news_video;
for(var i=0;i<videos.length;i++){
if(videos[i].is_video == 1 && videos[i].is_enable == 1 && videos[i].is_trash == 0){
  //console.log(videos[i]);
  $("#newsvideos").append('<video width="100%" controls src="https://ommcomnews.com/file/news/original/'+videos[i].file_link+'" poster="http://ommcomnews.com/file/news/original/'+videos[i].file_link.replace('.mp4','.jpg')+'"></video>');
$("#newsvideos").append("<div><strong><em>"+videos[i].name+"</em></strong></div>");
}
}
});
}
//////////////////////////////////////////////
function citizenj(){
citizenj_mv();
citizenj_latest();
}
/////////////////////////////////////////////
function citizenj_mv(){
var str='';
  $.ajax({
        url: "https://ommcomnews.com/api/v0.1/citizen/mostviewed"
    }).then(function(data) {
var counter=0;
      $.each(data, function(index, value) {
        counter++;
              str='';
              str="<div class='imgClass videoOuter'>";
              if(value.file_type != 'Video'){
                str= str + "<img src='https://ommcomnews.com/file/citizenNews/original/" + value.file_path + "' />";
$('#tab-item-1').append(str);
              }else{
                var file_image="https://ommcomnews.com/file/citizenNews/original/"+value.file_path.replace('.mp4','.jpg');
var vi='news_video'+counter;
                $('#tab-item-1').append("<video id=" +vi+ " width='100%' controls autoplay poster=" + file_image + "></video>");
                var newsv = document.getElementById('news_video'+counter);
                var newss = document.createElement('source');
                newss.setAttribute('src','https://ommcomnews.com/file/citizenNews/original/'+ value.file_path);
                newsv.appendChild(newss);
              }
                $('#tab-item-1').append("<h2>" + value.description + "</h2></div>");
                $('#tab-item-1').append("	<p class='time fl'><i class='fa fa-user fa-fw blueCol'></i>" + value.name + "</p>");
$('#tab-item-1').append("<p class='time fr' id='divtime"+counter+"'><i class='fa fa-clock-o fa-fw blueCol'></i></p>");
$('#tab-item-1').append("<div class='clear'></div></div>");
$('#tab-item-1').append("<p class='time'><i class='fa fa-eye fa-fw blueCol'></i> "+value.news_count+" Viewes</p></div>");
//console.log(value);
$('#divtime'+counter).append(findduration(value.updated_at));
            // console.log(value);
        });

    });
}
/////////////////////////
function citizenj_latest(){
  var str='';
    $.ajax({
          url: "https://ommcomnews.com/api/v0.1/citizen/latest"
      }).then(function(data) {
  var counter=0;
        $.each(data, function(index, value) {
          counter++;
                str='';
                str="<div class='imgClass videoOuter'>";
                if(value.file_type != 'Video'){
                  str= str + "<img src='https://ommcomnews.com/file/citizenNews/original/" + value.file_path + "' />";
  $('#tab-item-2').append(str);
                }else{
                  var file_image="https://ommcomnews.com/file/citizenNews/original/"+value.file_path.replace('.mp4','.jpg');
  var vi='news_video1'+counter;
                  $('#tab-item-2').append("<video id=" +vi+ " width='100%' controls autoplay poster=" + file_image + "></video>");
                  var newsv = document.getElementById('news_video1'+counter);
                  var newss = document.createElement('source');
                  newss.setAttribute('src','https://ommcomnews.com/file/citizenNews/original/'+ value.file_path);
                  newsv.appendChild(newss);
                }
                  $('#tab-item-2').append("<h2>" + value.description + "</h2></div>");
                  $('#tab-item-2').append("	<p class='time fl'><i class='fa fa-user fa-fw blueCol'></i>" + value.name + "</p>");
  $('#tab-item-2').append("<p class='time fr' id='divtimed"+counter+"'><i class='fa fa-clock-o fa-fw blueCol'></i></p>");
  $('#tab-item-2').append("<div class='clear'></div></div>");
  $('#tab-item-2').append("<p class='time'><i class='fa fa-eye fa-fw blueCol'></i> "+value.news_count+" Viewes</p></div>");
               //console.log(value);
               $('#divtimed'+counter).append(findduration(value.updated_at));
          });

      });
}
//////////////////////////////////
function votepolling(){
  $.ajax({
        url: "https://ommcomnews.com/api/v0.1/poll/question"
    }).then(function(data) {
    //  console.log(data.ANSWER);
      $('#pollq').append(data.QUESTION.name);
      $('#option1').append(data.ANSWER[1].name + ' (' + data.ANSWER[1].percentage + ' %)');
      $('#div34').attr('value',data.ANSWER[1].name);
      $('#option2').append(data.ANSWER[2].name + ' (' + data.ANSWER[2].percentage + ' %)');
      $('#div35').attr('value',data.ANSWER[2].name);
      $('#question_id').attr('value',data.QUESTION.id);
    });
}
//////////////////////////////////////
function catnews(slug,page){
var str='';
var poster='';
var counter=0;
  $.ajax({
        url: "https://ommcomnews.com/api/v0.1/posts/"+slug+"?page="+page
    }).then(function(data) {

      $.each(data, function(index, value) {
        $.each(value,function(index1,value1){
          counter++;
    //    console.log(value1);
        poster='';
str=str + "<li>";
var ps='';
if(value1.is_video != 1){
  str=str + "<div class='fl videoL'><img src='"+'https://ommcomnews.com/file/news/exact_350_224/'+value1.featured_image+"' alt='' title='' /></div>";
}else{
  poster=value1.file_path;
  poster=poster.replace('.mp4','.jpg');
  str=str + "<div class='fl videoL'>";
//str = str + "<video width='120px'  controls poster='"+'http://ommcomnews.com/public/file/news/exact_350_224/'+poster+"'>";
//str = str + "<source src='"+'http://ommcomnews.com/public/file/news/original/'+value1.file_path+"' type='video/mp4'>";
//str=str + "</video>";
poster = 'https://ommcomnews.com/file/news/exact_350_224/'+poster;
ps='newsdetail.html?slug='+value1.slug;
str = str + "<a href='"+ps+"'><img src='"+poster+"'  /></a>";
str = str + "</div>";
}
//str=str + "<div class='fl videoL'><img src='images/videosm.jpg' alt='' title='' /></div>";
str =str + "<div class='fr videoR'>";
str=str + "<a style='text-decoration:none' href='newsdetail.html?slug="+value1.slug+"'>";
str =str + "<h3>"+value1.name.substring(0, 25)+"....</h3></a>";
str =str + "<p class='time' id='cattime"+counter+"'><i class='fa fa-clock-o fa-fw'></i></p>";
str = str + "</div><div class='clear'></div></li>";
$('#newslist').append(str);
$('#cattime'+counter).append(findduration(value1.approved_date));
str='';
        });

});
    });
}
/////////////////////////////////////////
function findduration(str){
  var datePart = str.match(/\d+/g),
   year = '20' + datePart[0].substring(2), // get only two digits
   month = datePart[1], day = datePart[2];
  var date1=new Date(year,month-1,day);
   var _MS_PER_DAY = 1000 * 60 * 60 * 24;
   var utc1 = Date.UTC(date1.getFullYear(), date1.getMonth(), date1.getDate());
var utc2=new Date();
   utc2=new Date(utc2.getFullYear(),utc2.getMonth(),utc2.getDate());
   utc2 = Date.UTC(utc2.getFullYear(), utc2.getMonth(), utc2.getDate());
   var days= Math.floor((utc2 - utc1) / _MS_PER_DAY);
if(days == 0){
  return "Today";
}
if(days == 1){
  return "1 Day ago";
}
if(days >= 2){
  return days + ' Days ago';
}
}
//////////////////////////////////////////////
function loadmoretopnews(pageno){
  var stop=0;
  var str="";
  var image='';
  $.ajax({
        url: "https://ommcomnews.com/api/v0.1/posts/nextTopNews?page="+pageno
    }).then(function(data) {
        $.each(data, function(index, value) {
        //  $.each(value,function(index1,value1){

          if(index == "last_page"){
          if(str <= value){
              stop=0;
          }else{
            stop=1;
          }
        }

if(stop == 0){
  if(index == "data"){
    $.each(value,function(index1,value1){
        //console.log(index1);
        //console.log(value1);
        str="";
        image='';
        str="<li>";
        str=str + "<div class='fl videoL'><div class='' id='top5_media'>";
        //console.log(value1);
          if(value1.is_video == 1){
                image="https://ommcomnews.com/file/news/exact_375_200/"+value1.file_path;
                image=image.replace("mp4","jpg");
          }else{
            image="https://ommcomnews.com/file/news/exact_375_200/"+value1.featured_image;
          }
          if(image == "https://ommcomnews.com/file/news/exact_375_200/null"){
            image="images/blank.jpg";
          }
        str= str +
        "<a href='newsdetail.html?slug="+value1.slug+"'><img src='"+image+"' alt='' title='' /></a></div></div>";
        str =str + "<div class='fr videoR'><h3 id='top5_title'><a href='newsdetail.html?slug="+value1.slug+"' id='top1_href' style='text-decoration:none;font-size:15px'>"+value1.name.substring(0, 25)+"....</a></h3>";
        str=str + "	<p class='time' id='appdate5'><i class='fa fa-clock-o fa-fw'></i>"+findduration(value1.approved_date)+"</p>";
        str=str + "</div><div class='clear'></div></li>";
        $(".videoList").append(str);

    });
  }

}else{
  alert("No more news found !");
}

      //  });
        });
    });
    // $("html, body").animate({ scrollTop: $(document).height() }, 2000);
}
//////////////////////////
function catnewsmore(slug,page){
  var stop=0;
  var str="";
  var image='';
  $.ajax({
        url: "https://ommcomnews.com/api/v0.1/posts/nextCategory/"+slug+"?page="+page
    }).then(function(data) {
        $.each(data, function(index, value) {
        //  $.each(value,function(index1,value1){

          if(index == "last_page"){
          if(str <= value){
              stop=0;
          }else{
            stop=1;
          }
        }

if(stop == 0){
  if(index == "data"){
    $.each(value,function(index1,value1){
        //console.log(index1);
        //console.log(value1);
        str="";
        image='';
        str="<li>";
        str=str + "<div class='fl videoL'><div class='' id='top5_media'>";
        //console.log(value1);
          if(value1.is_video == 1){
                image="https://ommcomnews.com/file/news/exact_375_200/"+value1.file_path;
                image=image.replace("mp4","jpg");
          }else{
            image="https://ommcomnews.com/file/news/exact_375_200/"+value1.featured_image;
          }
          if(image == "https://ommcomnews.com/file/news/exact_375_200/null"){
            image="images/blank.jpg";
          }
        str= str +
        "<a href='newsdetail.html?slug="+value1.slug+"'><img src='"+image+"' alt='' title='' /></a></div></div>";
        str =str + "<div class='fr videoR'><h3 id='top5_title'><a href='newsdetail.html?slug="+value1.slug+"' id='top1_href' style='text-decoration:none;font-size:15px'>"+value1.name.substring(0, 25)+"....</a></h3>";
        str=str + "	<p class='time' id='appdate5'><i class='fa fa-clock-o fa-fw'></i>"+findduration(value1.approved_date)+"</p>";
        str=str + "</div><div class='clear'></div></li>";
        $(".videoList").append(str);

    });
  }

}else{
  alert("No more news found !");
}

      //  });
        });
    });
     $("html, body").animate({ scrollTop: $(document).height() }, 2000);
}
//////////////////////////////////////////////
function searchnews(slug){
  var stop=0;
  var str="";
  var image='';
  var ex=0;
  $.ajax({
        url: "https://ommcomnews.com/api/v0.1/posts/searchNewsList/"+slug
    }).then(function(data) {
        $.each(data, function(index, value) {
  if(index == "data"){
    $.each(value,function(index1,value1){
        //console.log(index1);
        //console.log(value1);
        str="";
        image='';

        str="<li>";
        str=str + "<div class='fl videoL'><div class='' id='top5_media'>";
        //console.log(value1);
          if(value1.is_video == 1){
                image="https://ommcomnews.com/file/news/exact_375_200/"+value1.file_path;
                image=image.replace("mp4","jpg");
          }else{
            image="https://ommcomnews.com/file/news/exact_375_200/"+value1.featured_image;
          }
          if(image == "https://ommcomnews.com/file/news/exact_375_200/null"){
            image="images/blank.jpg";
          }
        str= str +
        "<a href='newsdetail.html?slug="+value1.slug+"'><img src='"+image+"' alt='' title='' /></a></div></div>";
        str =str + "<div class='fr videoR'><h3 id='top5_title'><a href='newsdetail.html?slug="+value1.slug+"' id='top1_href' style='text-decoration:none;font-size:15px'>"+value1.name.substring(0, 25)+"....</a></h3>";
        str=str + "	<p class='time' id='appdate5'><i class='fa fa-clock-o fa-fw'></i>"+findduration(value1.approved_date)+"</p>";
        str=str + "</div><div class='clear'></div></li>";
        $(".videoList").append(str);
    ex=ex + 1;
    });
  }

        });
    });
     //$("html, body").animate({ scrollTop: $(document).height() }, 2000);
    // if ( $('.videoList li').length == 0 ) {
    //   alert("Sorry ! No result found.");
    //   document.location="search.html";
    // }
}
//////////////////////////////////////////////////////
function odishaplus(){

  $.ajax({
        url: "https://ommcomnews.com/api/v0.1/posts/odishaPlus"
    }).then(function(data) {
        var image7='';
          $("#cat7_desc").append('<a style="color:white;text-decoration:none" href="newsdetail.html?slug='+data.data[0].slug+'">'+data.data[0].name+'</a>');
          $("#cat7_title").append("Odisha Plus");
            if(data.data[0].is_video == 1){
              image7=data.data[0].file_path;

              image7=image7.replace("mp4","jpg");
            }else{
              image7=data.data[0].featured_image;
            }

if(image7 != '' && image7 != null){
  image7="https://ommcomnews.com/file/news/exact_375_200/"+image7;
}else{
  image7="images/blank.jpg";
}
      $("#cat7_image").attr('src',image7);

    });
}
//////////////////////////////////////////
function loadcatlist(slug,counter){
  $.ajax({
        url: "https://ommcomnews.com/api/v0.1/posts/odishaPlus"
    }).then(function(data) {
      $("#catmain").append(data.data[0].name);
      $("#mainduration").append(findduration(data.data[0].approved_date));
      $("#mainlink").attr('href',"newsdetail.html?slug="+data.data[0].slug);
      var mainimage='';
      if(data.data[0].is_video == 1){
        mainimage=data.data[0].file_path;

        mainimage=mainimage.replace("mp4","jpg");
      }else{
        mainimage=data.data[0].featured_image;
      }

if(mainimage != '' && mainimage != null){
mainimage="https://ommcomnews.com/file/news/exact_375_200/"+mainimage;
}else{
mainimage="images/blank.jpg";
}
$("#mainimage").attr('src',mainimage);
    });
}
///////////////////////////
function loadmoreop(slug,counter){
  $.ajax({
        url: "https://ommcomnews.com/api/v0.1/posts/odishaPlus?page="+counter
    }).then(function(data) {
    var  str='';
    //  alert(data.data.length);
          for(var i=1;i<data.data.length;i++){
                str=str+"<li><a href='newsdetail.html?slug="+data.data[i].slug+"'><div class='fl videoL'>";
                var mainimage='';
                if(data.data[i].is_video == 1){
                  mainimage=data.data[i].file_path;
                  mainimage=mainimage.replace("mp4","jpg");
                }else{
                  mainimage=data.data[i].featured_image;
                }
                if(mainimage != '' && mainimage != null){
                mainimage="https://ommcomnews.com/file/news/exact_375_200/"+mainimage;
                }else{
                mainimage="images/blank.jpg";
                }
                str=str+"<img src='"+mainimage+"' /></div>";
                str=str+"	<div class='fr videoR'><h3>"+data.data[i].name+"</h3>";
            		str=str+"<p class='time'><i class='fa fa-clock-o fa-fw'></i>"+findduration(data.data[i].approved_date)+"</p>";
            		str=str+"</div><div class='clear'></div></a></li>";
            		$(".videoList").append(str);
            str='';
          }
    });
}
//////////////////////////////////////////
function loadcatlist1(slug,counter){
  $.ajax({
        url: "https://ommcomnews.com/api/v0.1/posts/"+slug
    }).then(function(data) {
      $("#catmain").append(data.data[0].name);
      $("#mainduration").append(findduration(data.data[0].approved_date));
      $("#mainlink").attr('href',"newsdetail.html?slug="+data.data[0].slug);
      var mainimage='';
      if(data.data[0].is_video == 1){
        mainimage=data.data[0].file_path;

        mainimage=mainimage.replace("mp4","jpg");
      }else{
        mainimage=data.data[0].featured_image;
      }

if(mainimage != '' && mainimage != null){
mainimage="https://ommcomnews.com/file/news/exact_375_200/"+mainimage;
}else{
mainimage="images/blank.jpg";
}
$("#mainimage").attr('src',mainimage);
    });
}
/////////////////////////////////////
function loadmoreop1(slug){
  $.ajax({
        url: "https://ommcomnews.com/api/v0.1/posts/"+slug
    }).then(function(data) {
    var  str='';
    //  alert(data.data.length);
          for(var i=1;i<data.data.length;i++){
                str=str+"<li><a href='newsdetail.html?slug="+data.data[i].slug+"'><div class='fl videoL'>";
                var mainimage='';
                if(data.data[i].is_video == 1){
                  mainimage=data.data[i].file_path;
                  mainimage=mainimage.replace("mp4","jpg");
                }else{
                  mainimage=data.data[i].featured_image;
                }
                if(mainimage != '' && mainimage != null){
                mainimage="https://ommcomnews.com/file/news/exact_375_200/"+mainimage;
                }else{
                mainimage="images/blank.jpg";
                }
                str=str+"<img src='"+mainimage+"' /></div>";
                str=str+"	<div class='fr videoR'><h3>"+data.data[i].name+"</h3>";
            		str=str+"<p class='time'><i class='fa fa-clock-o fa-fw'></i>"+findduration(data.data[i].approved_date)+"</p>";
            		str=str+"</div><div class='clear'></div></a></li>";
            		$(".videoList").append(str);
            str='';
          }
    });
}
////////////////////////////////////////////////////////
function loadviral1(){
  $.ajax({
        url: "https://ommcomnews.com/api/v0.1/viralVideos"
    }).then(function(data) {
      $("#catmain").append(data.data[0].name);
      $("#mainduration").append(findduration(data.data[0].approved_date));
      $("#mainlink").attr('href',"newsdetail.html?slug="+data.data[0].slug);
      var mainimage='';
      if(data.data[0].is_video == 1){
        mainimage=data.data[0].file_path;

        mainimage=mainimage.replace("mp4","jpg");
      }else{
        mainimage=data.data[0].featured_image;
      }

if(mainimage != '' && mainimage != null){
mainimage="https://ommcomnews.com/file/news/exact_375_200/"+mainimage;
}else{
mainimage="images/blank.jpg";
}
$("#mainimage").attr('src',mainimage);
    });
}
////////////////////////////////////////////////////////
function loadviral2(){
  $.ajax({
          url: "https://ommcomnews.com/api/v0.1/viralVideos"
    }).then(function(data) {
    var  str='';
    //  alert(data.data.length);
          for(var i=1;i<data.data.length;i++){
                str=str+"<li><a href='newsdetail.html?slug="+data.data[i].slug+"'><div class='fl videoL'>";
                var mainimage='';
                if(data.data[i].is_video == 1){
                  mainimage=data.data[i].file_path;
                  mainimage=mainimage.replace("mp4","jpg");
                }else{
                  mainimage=data.data[i].featured_image;
                }
                if(mainimage != '' && mainimage != null){
                mainimage="https://ommcomnews.com/file/news/exact_375_200/"+mainimage;
                }else{
                mainimage="images/blank.jpg";
                }
                str=str+"<img src='"+mainimage+"' /></div>";
                str=str+"	<div class='fr videoR'><h3>"+data.data[i].name+"</h3>";
            		str=str+"<p class='time'><i class='fa fa-clock-o fa-fw'></i>"+findduration(data.data[i].approved_date)+"</p>";
            		str=str+"</div><div class='clear'></div></a></li>";
            		$(".videoList").append(str);
            str='';
          }
    });
}
/////////////////////
function logo_click(){
    setTimeout( function () {
        window.stop();
        document.location='home.html';
    } , 100 );
}

function shistory(){
  setTimeout( function () {
      window.stop();
      history.go(-1);
  } , 100 );
}
