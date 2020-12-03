<template>
  <div id="page">

    <div class="main">

      <div class="line"></div>
      <el-menu
        :default-active="activeIndex"
        class="el-menu-demo"
        mode="horizontal"
        @select="handleSelect"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b">
        <el-submenu index="1">
          <template slot="title">Profile</template>
          <el-menu-item index="1-1">details</el-menu-item>
          <el-menu-item index="1-2">change password</el-menu-item>
        </el-submenu>
        <el-menu-item index="2">{{$route.query.email}}</el-menu-item>
        <el-menu-item index="3" @click="logout">Log out</el-menu-item>
        <el-menu-item index="4">
          <el-input v-model="searchInput" placeholder="search keywords"></el-input>
          <!--pop over-->
          <el-popover
            @hide="renew"
            width="1000px"
            trigger="click">
            <el-table :data="gridData">
              <el-table-column width="150" property="title" label="title"></el-table-column>
              <el-table-column width="200" property="start" label="start"></el-table-column>
              <el-table-column width="200" property="end" label="end"></el-table-column>
              <el-table-column width="200" property="location" label="location"></el-table-column>
              <el-table-column width="200" property="description" label="description"></el-table-column>
            </el-table>
            <el-button slot="reference" @click="search">Search</el-button>
          </el-popover>

        </el-menu-item>
      </el-menu>


      <!--   calendar   -->
      <FullCalendar defaultView="dayGridMonth"
                    timeZone="UTC"
                    firstDay="1"
                    weekNumberCalculation="ISO"
                    editable="true"
                    droppable="true"
                    displayEventEnd="true"
                    :eventTimeFormat="eventTime"
                    :header="header"
                    :plugins="calendarPlugins"
                    :events="calendarEvents"
                    @dateClick="handleDateClick"
                    @eventClick="handleEventClick"
                    @eventDrop="calendarEventDrop"
                    @datesRender="handleDatesRender"
      />
    </div>

    <footer>
      <p><a href="https://github.com/haoboliu66">github.com/haoboliu66</a></p>
    </footer>

    <el-dialog :title="optTitle" :visible.sync="dialogFormVisible">
      <el-form :model="eventForm">
        <el-form-item label="Event Title" label-width="80px">
          <el-input v-model="eventForm.title" auto-complete="off" placeholder="Add Title"></el-input>
        </el-form-item>
        <el-form-item label="Start Time" label-width="80px">
          <el-date-picker
            v-model="eventForm.start"
            type="datetime"
            format="yyyy-MM-dd HH:mm:ss"
            placeholder="Select Start Time">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="End Time" label-width="80px">
          <el-date-picker
            v-model="eventForm.end"
            type="datetime"
            format="yyyy-MM-dd HH:mm:ss"
            placeholder="Select End Time">
          </el-date-picker>
        </el-form-item>
        <!--     location   -->
        <el-form-item label="Location" label-width="80px">
          <el-input v-model="eventForm.location" auto-complete="off" placeholder="Add Location"></el-input>
        </el-form-item>
        <!--      event description-->
        <el-form-item label="Details" label-width="80px">
          <el-input type="textarea" :autosize="{ minRows: 1, maxRows: 3}" v-model="eventForm.description"
                    auto-complete="off"
                    placeholder="Add Description"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="warning" @click="delEvent" v-if="eventForm.id" style="float: left;">Delete</el-button>
        <el-button @click="clearForm">Cancel</el-button>
        <el-button type="primary" @click="saveEvent">Confirm</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>
  import FullCalendar from '@fullcalendar/vue';
  import dayGridPlugin from '@fullcalendar/daygrid';
  import timeGridPlugin from '@fullcalendar/timegrid';
  import interactionPlugin from '@fullcalendar/interaction';
  import listPlugin from '@fullcalendar/list';

  import '@fullcalendar/core/main.css';
  import '@fullcalendar/daygrid/main.css';
  import '@fullcalendar/timegrid/main.css';
  import '@fullcalendar/list/main.css';
  import QS from 'qs';
  import axios from "axios";
  import api from "../api";
  import moment from 'moment'
  import store from "../store/store";

  export default {

    constructor() {
      const name = Calendar.name; // add this line in your constructor
    },

    components: {
      FullCalendar,
    },
    data() {
      return {
        activeIndex: '1',

        innerEvent: '',

        calendarPlugins: [
          dayGridPlugin,
          timeGridPlugin,
          interactionPlugin,
          listPlugin
        ],
        header: {
          left: 'prev,next today',
          center: 'title',
          right: 'dayGridMonth,timeGridWeek,timeGridDay,listWeek'
        },
        eventTime: {
          hour: 'numeric',
          minute: '2-digit',
          hour12: false
        },

        calendarEvents: [],
        calendarEventDrop: info => {
          this.dropEvent(info);
        },
        handleDatesRender: arg => {
          // this.getEventsList(arg.view)
        },

        dialogFormVisible: false,
        eventForm: {
          title: "",
          start: "",
          end: "",
          location: "",
          description: "",
          accountId: ""
        },
        optTitle: '添加事件',
        searchInput: '',
        gridData: []


      }
    },
    mounted() {

      window.addEventListener('beforeunload', () => {
        console.log("before refresh");
        this.$store.commit('setEvents', {events: this.calendarEvents});
        sessionStorage.setItem('token', JSON.stringify(this.$store.state.token));
      })

      let cached = store.getters.getEvents;
      let events = JSON.parse(cached).events;
      console.log(events);
      if(events.length > 0){
        this.calendarEvents = events;
        return;
      }

      //check login state
      if (store.getters.getToken) {
        let id = this.$route.query.id
        this.eventForm.accountId = id;
        // get all events of the user
        this.getEventsList(id);

      }

    },
    created() {


    },
    methods: {
      handleSelect(key, keyPath) {
        console.log(key, keyPath);
      },

      getEventsList(id) {
        let params = 'id=' + id;
        axios
          .post(api.ListEvent, params)
          .then(res => {
            console.log(res)
            let data = res.data.result;
            for (let i = 0; i < data.length; i++) {
              let item = data[i];
              // let oldEvent = {
              //   id: item.id,
              //   title: item.title,
              //   start: item.start,
              //   end: item.end,
              //   location: item.location,
              //   description: item.description,
              //   accountId: item.accountId
              // };
              this.calendarEvents.push(item);
            }
          })

      },
      //create new event
      handleDateClick(arg) {
        this.dialogFormVisible = true;
        this.optTitle = 'New Event';
        this.eventForm.title = '';
          this.eventForm.id = '';
          this.eventForm.start = arg.date;
        this.eventForm.end = arg.date;
        this.eventForm.location = '';
          this.eventForm.description = '';
      },

      // modify event
      handleEventClick(info) {
        info.el.style.borderColor = 'red';
        this.dialogFormVisible = true;
        this.optTitle = 'Modify Event';
        this.eventForm = {
          id: info.event.id,
          title: info.event.title,
          start: info.event.start,
          end: info.event.end,
          location: info.event.extendedProps.location,
          description: info.event.extendedProps.description,
          accountId: this.eventForm.accountId,
        };
      },
      // save an event
      saveEvent() {
        // attach account id
        this.eventForm.accountId = this.$route.query.id;

        // created new event
        const newEvent = {
          id: '',
          title: this.eventForm.title,
          start: this.eventForm.start,
          end: this.eventForm.end,
          location: this.eventForm.location,
          description: this.eventForm.description,
          accountId: this.eventForm.accountId
        };

        if(newEvent.title == null || newEvent.title == ""){
          this.$message.error("incomplete form: Title required");
          return;
        }

        if(newEvent.start == ""|| newEvent.start == null){
          this.$message.error("incomplete form: Start Date required");
          return;
        }

        if(newEvent.end == ""|| newEvent.end == null){
          this.$message.error("incomplete form: End Date required");
          return;
        }

        this.innerEvent = '';

        // change date format
        this.eventForm.start = moment(this.eventForm.start).format('YYYY-MM-DD hh:mm:ss');
        this.eventForm.end = moment(this.eventForm.end).format('YYYY-MM-DD hh:mm:ss');

        /*
        {
          headers: {'Content-Type':'application/json' }
        }
         */
        axios.post(api.AddEvent, QS.stringify(this.eventForm))
          .then((res) => {
            console.log(res);
            if (res.data.code === 200) {
              if (this.eventForm.id === "" || this.eventForm.id === undefined) { //add new event
                this.eventForm.id = res.data.result.id; //update event id
                newEvent.id = this.eventForm.id;
                this.calendarEvents.push(newEvent);
                this.$message.success("add success");
              } else { //Modify
                console.log("modify!");
                this.calendarEvents.forEach((item, index, arr) => {
                  if (item.id == this.eventForm.id) {
                    arr[index].title = this.eventForm.title;
                    arr[index].start = this.eventForm.start;
                    arr[index].end = this.eventForm.end;
                    arr[index].location = this.eventForm.location;
                    arr[index].description = this.eventForm.location;
                  }
                });
                this.$message.success("modify success");
              }

              this.dialogFormVisible = false;
            } else {
              this.$message.error("add event fail! ");
            }
          });
      },
      //Delete event
      delEvent() {
        let params = 'id=' + this.eventForm.id;

        this.$post(api.DeleteEvent, params)
          .then((res) => {
            if (res.code === 200) {
              this.calendarEvents.forEach((item, index, arr) => {
                if (item.id == this.eventForm.id) {
                  arr.splice(index, 1);
                }
              });
              this.dialogFormVisible = false;
              this.$message({
                message: '删除成功！',
                type: 'success'
              });
            } else {
              this.$message({
                message: res.message,
                type: 'error'
              });
            }
          });
      },
      //拖动事件
      dropEvent(info) {
        this.eventForm = {
          id: info.event.id,
          title: info.event.title,
          start: info.event.start,
          end: info.event.end
        };
        this.saveEvent();
      },

      clearForm() {
        this.eventForm.title = '',
          this.eventForm.location = '',
          this.eventForm.description = '',
          this.dialogFormVisible = false
      },

      // search events by keywords
      search() {
        console.log("search!");
        let keyword = this.searchInput;
        console.log(keyword);

        if(keyword == ""){  //keyword cannot be empty
          return;
        }

        this.$axios({
          method : 'GET',
        url : api.SearchEvent,
          params:{
          keywords : keyword,
        }

      }).then(res =>{
          console.log(res);
          let data = res.data.result;
          for(let i = 0; i < data.length; i++){
            let item = data[i];
            let popover = {
              title: item.title,
              start: item.start,
              end: item.end,
              location: item.location,
              description: item.description
            };
            this.gridData.push(popover);
          }


        }).catch(err =>{

        })

      },
      // clear pop-over table
      renew(){
        this.gridData = [];
      },
      // user log out
      logout() {
        axios
          .get(api.Logout)
          .then(res => {
            console.log(res);

            console.log(store.state.token)
            //clear token from sessionStorage
            this.$store.commit('del_token')
            this.$router.push('/');
          })
      }
    }
  }

</script>

<style>
  html {
    /*background: url(../assets/bg.png);*/
  }

  body {
    margin: 0;
    padding: 0;
    /*background: url(../assets/body_bg.gif) repeat-x;*/
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

  .logo a {
    display: block;
    width: 100%;
    height: 100%;
  }

  footer {
    padding-top: 20px;
    height: 60px;
    text-align: center;
  }

  #page p {
    text-align: center;
    line-height: 42px;
  }

  #page h2 {
    padding-top: 20px;
    text-align: center;
    font-size: 20px;
  }

  .main {
    margin: 10px;
    text-align: center;
    background: #fff;
    padding: 10px;
  }
</style>
