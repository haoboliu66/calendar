<template>
    <div id="page">
        <header>
            <h1 class="logo"><a href="http://www.helloweba.net" title="返回helloweba首页">helloweba</a></h1>
        </header>
      <h1>213213</h1>
        <h2><a href="http://www.helloweba.net/javascript/621.html">在Vue框架下使用Fullcalendar</a></h2>
        <div class="main">
            <FullCalendar defaultView="dayGridMonth" locale="zh-cn" firstDay="1" weekNumberCalculation="ISO"
            showNonCurrentDates="false"
            :eventTimeFormat="evnetTime"
            :header="header"
            @dateClick="handleDateClick"
            :plugins="calendarPlugins"
            :events="calendarEvents"
            @eventClick="handleEventClick"
             />
        </div>

        <footer>
            <p>Powered by helloweba.net  允许转载、修改和使用本站的DEMO，但请注明出处：<a href="http://www.helloweba.net">www.helloweba.net</a></p>
            <p class="hidden"></p>
        </footer>
    </div>
</template>

<script>
import FullCalendar from '@fullcalendar/vue'
import dayGridPlugin from '@fullcalendar/daygrid'
import interactionPlugin from '@fullcalendar/interaction'
import '@fullcalendar/core/main.css';

export default {
    components: {
        FullCalendar
    },
    data() {
        return {
            calendarPlugins: [
                dayGridPlugin,
                interactionPlugin // needed for dateClick
            ],
            header: {
                left: 'prev,next today',
                center: 'title',
                right: 'dayGridMonth,timeGridWeek,timeGridDay'
            },
            evnetTime: {
                hour: 'numeric',
                minute: '2-digit',
                hour12: false
            },
            calendarEvents: [ // initial event data
                { title: '部门会议', start: new Date() }
            ]
        }
    },
    mounted() {

    },
    created() {

    },
    methods: {
        handleDateClick(arg) {
            if (confirm('您是否要在' + arg.dateStr + '添加一个新的事件?')) {
                this.calendarEvents.push({ // add new event data
                  title: '新的事件',
                  startTime: arg.date,
                  allDay: arg.allDay
                })
            }
        },
        handleEventClick(info) {
            alert('Event: ' + info.event.title);
            //alert('Coordinates: ' + info.jsEvent.pageX + ',' + info.jsEvent.pageY);
            //alert('View: ' + info.view.type);

            // change the border color just for fun
            info.el.style.borderColor = 'red';
        }
    }
}
</script>
<style>
html {
    background: url(../assets/bg.png);
}
body {
    background: url(../assets/body_bg.gif) repeat-x;
}
header {
    box-sizing: border-box;
    width: 100%;
    height: 100px;
    padding: 10px;
    overflow: hidden;
}
.logo {
    width: 70px;
    height: 70px;
    margin: 0 auto;
    background: url(../assets/logo.png) no-repeat 0 10px;
    text-indent: -999em;
}
.logo a{
    display: block;
    width: 100%;
    height: 100%;
}

footer {
    padding-top: 20px;
    height: 60px;
    text-align: center;
}
#page p{
    text-align: center;
    line-height: 42px;
}
#page h2{
    padding-top: 20px;
    text-align: center;
    font-size: 20px;
}
.main{
    margin: 10px;
    text-align: center;
    background: #fff;
    padding: 10px;
}
</style>
