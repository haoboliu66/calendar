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
          <template slot="title">Shared</template>
          <el-menu-item index="1-2" @click="showReceivedTable">Received Events</el-menu-item>
        </el-submenu>
        <el-menu-item index="2">{{$route.query.email}}</el-menu-item>
        <el-menu-item index="3" @click="logout">Log out</el-menu-item>
        <el-menu-item index="4">
          <el-input v-model="searchInput" placeholder="search keywords"></el-input>
          <!--pop over for search result-->
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
              <el-table-column width="200" property="owner" label="owner"></el-table-column>
            </el-table>
            <el-button slot="reference" icon="el-icon-search" @click="search">Search</el-button>
          </el-popover>

          <!--checkbox for share all events-->
          <el-checkbox style="padding-left: 20px; color: white" v-model="shareAllChecked" @change="shareAllSelected">All
            Events
          </el-checkbox>

          <!--       share all button   -->
          <el-popover
            placement="top"
            width="160"
            style="padding-left: 10px"
            v-model="shareAllButtonVisible">
            <p>Share all events?</p>
            <el-input v-model="receiver" placeholder="Username/Email" style="width: 100%"></el-input>
            <div style="text-align: right; margin: 0">
              <el-button size="mini" type="text" @click="shareAllButtonVisible = false">cancel</el-button>
              <el-button type="primary" size="mini" @click="shareAll">confirm</el-button>
            </div>
            <el-button :disabled="buttonDisable" slot="reference">Share All</el-button>
          </el-popover>

        </el-menu-item>
      </el-menu>


      <!--    received events table   -->
      <el-table stripe
                v-bind:class="{receivedTableHide: hideReceivedTable,receivedTableShow: !hideReceivedTable}"
                max-height="250px"
                :data="receivedEvents">
        <el-table-column fixed width="200" property="title" label="title"></el-table-column>
        <el-table-column width="170" property="start" label="start"></el-table-column>
        <el-table-column width="170" property="end" label="end"></el-table-column>
        <el-table-column width="150" property="location" label="location"></el-table-column>
        <el-table-column width="200" property="description" label="description"></el-table-column>
        <el-table-column width="85" property="accountId" label="ownerId"></el-table-column>
        <el-table-column width="200" property="email" label="email"></el-table-column>
      </el-table>


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
        <!--         @click="delEvent" -->
        <!--        <el-button type="warning" slot="reference" v-if="eventForm.id" style="float: left;">Delete</el-button>-->


        <el-button @click="clearForm">Cancel</el-button>


        <!--     share one event button   -->
        <el-dialog
          class="shareOne"
          title="Enter Receiver"
          :visible.sync="dialogVisible"
          width="30%"
          :modal="modal"
          center>
          <el-input v-model="receiver" placeholder="Enter Username/Email"></el-input>
          <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">Cancel</el-button>
    <el-button type="primary" @click="shareOneEvent">Confirm</el-button>
            <!--     dialogVisible = false  -->
  </span>
        </el-dialog>
        <el-button v-if="eventForm.id" @click="dialogVisible = true">Share</el-button>

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

        deletePopOver: false,

        shareAllChecked: false, // shareAll checkbox
        receiver: '',
        shareAllButtonVisible: false, // for shareAll button
        buttonDisable: true,
        sharedEvents: [],

        receivedEvents: [], // for received events from other users
        hideReceivedTable: true, // received events table visibility

        editingEvent: '',

        dialogVisible: false, // share button in eventForm
        modal: false,  // cancel mask when dialog displays

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
        optTitle: '',
        searchInput: '',
        gridData: []
      }
    },
    mounted() {
    },
    activated() {
      window.addEventListener('beforeunload', () => {
        this.$store.commit('setEvents', {events: this.calendarEvents});
        localStorage.setItem('token', JSON.stringify(this.$store.state.token));
      })

      // --------------------add window reload listener----------------------------------


      const id = this.$route.query.id
      let cached = store.getters.getEvents;
      // if no cache, retrieve from backend
      if (cached == null || cached == undefined || cached.length == 0) {
        //check login state
        if (store.getters.getToken) {
          // get all events of the user
          this.getEventsList(id);
        }
      } else {
        // if cache exists, render events
        let events = JSON.parse(cached).events;
        console.log(events);
        if (events.length > 0) {
          console.log("load from store");
          this.calendarEvents = events;
        } else {
          this.eventForm.accountId = id;
          // get all events of the user
          this.getEventsList(id);
        }
      }

      // get shared events by current account id
      this.eventForm.accountId = id;
      let params = 'id=' + id;
      axios.post(api.GetSharedEvent, params)
        .then(res => {
          let shared = res.data.result;
          for (let i = 0; i < shared.length; i++) {
            this.receivedEvents.push(shared[i]);
          }
        })

    },

    methods: {
      handleSelect(key, keyPath) {
        // console.log(key, keyPath);
      },

      getEventsList(id) {
        let params = 'id=' + id;
        axios
          .post(api.ListEvent, params)
          .then(res => {
            let data = res.data.result;
            for (let i = 0; i < data.length; i++) {
              let item = data[i];
              this.calendarEvents.push(item);
            }
          })
      },
      /**
       * create new event by filling in form
       */
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

      /**
       *  modify event
       */
      handleEventClick(info) {
        if (info.event.end == null || info.event.end == undefined || info.event.end == "") {
          // fill in end date(drawback of fullCalendar)
          let targetId = info.event.id;
          let events = this.calendarEvents;
          for (let i = 0; i < events.length; i++) {
            let item = events[i];
            if (item.id == targetId) {
              info.event.setEnd(item.end);
              break;
            }
          }
        }

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

        // store editing event for share
        this.editingEvent = this.eventForm;

      },
      /**
       * save an event
       */
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

        // check data completeness
        if (newEvent.title == null || newEvent.title == "") {
          this.$message.error({message: "incomplete form: Title required", duration: 2000});
          return;
        }
        // check data completeness
        if (newEvent.start == "" || newEvent.start == null) {
          this.$message.error({message: "incomplete form: Start Date required", duration: 2000});
          return;
        }
        // check data completeness
        if (newEvent.end == "" || newEvent.end == null) {
          this.$message.error({message: "incomplete form: End Date required", duration: 2000});
          return;
        }
        if (newEvent.end.toString() == newEvent.start.toString()) {
          this.$message.error({message: "Start and End cannot be the same", duration: 2000});
          return;
        }

        this.innerEvent = '';

        // change date format ,   hh:mm:ss(12)   HH:mm:ss(24)
        this.eventForm.start = moment(this.eventForm.start).format('YYYY-MM-DD HH:mm:ss');
        this.eventForm.end = moment(this.eventForm.end).format('YYYY-MM-DD HH:mm:ss');
        /*
        {
          headers: {'Content-Type':'application/json' }
        }
         */
        axios.post(api.AddEvent, QS.stringify(this.eventForm))
          .then((res) => {
            if (res.data.code === 200) {
              if (this.eventForm.id === "" || this.eventForm.id === undefined) { //add new event
                this.eventForm.id = res.data.result.id; //update event id
                newEvent.id = this.eventForm.id;
                // newEvent.start = this.eventForm.start;
                // newEvent.end = this.eventForm.end;
                this.calendarEvents.push(newEvent);
                this.$message.success({message: "add success", duration: 2000});
              } else {
                //Modify
                console.log("modify event!");
                this.calendarEvents.forEach((item, index, arr) => {
                  if (item.id == this.eventForm.id) {
                    arr[index].title = this.eventForm.title;
                    arr[index].start = this.eventForm.start;
                    arr[index].end = this.eventForm.end;
                    arr[index].location = this.eventForm.location;
                    arr[index].description = this.eventForm.description;
                  }
                });
                this.$message.success({message: "modify success", duration: 2000});
                //update store
                this.$store.commit('setEvents', {events: this.calendarEvents});
              }

              this.dialogFormVisible = false;
            } else {
              this.$message.error({message: "add event fail! ", duration: 2000});
            }
          });
      },
      /**
       * Delete event
       */
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
                message: 'Delete success！',
                type: 'success',
                duration: 2000
              });
            } else {
              this.$message({
                message: "Error",
                type: 'error',
                duration: 2000
              });
            }
          });
      },
      /**
       * drag an event
       */
      dropEvent(info) {
        let targetId = info.event.id;
        let events = this.calendarEvents;
        for (let i = 0; i < events.length; i++) {
          let item = events[i];
          if (item.id == targetId) {
            info.event.setEnd(item.end);
            break;
          }
        }
        this.eventForm = {
          id: info.event.id,
          title: info.event.title,
          start: info.event.start,
          end: info.event.end,
          location: info.event.extendedProps.location,
          description: info.event.extendedProps.description,
        };
        this.saveEvent();
      },
      /**
       * search events by keywords
       */
      search() {
        let keyword = this.searchInput;
        //keyword cannot be empty
        if (keyword == "" || keyword == undefined || keyword == null) {
          return;
        }
        this.$axios({
          method: 'GET',
          url: api.SearchEvent,
          params: {
            keywords: keyword,
            accountId: this.eventForm.accountId
          }
        }).then(res => {
          let data = res.data.result;
          for (let i = 0; i < data.length; i++) {
            let item = data[i];
            let popover = {
              title: item.title,
              start: item.start,
              end: item.end,
              location: item.location,
              description: item.description,
              owner: item.accountId
            };
            // push to pop-over table for display after searching
            this.gridData.push(popover);
          }
        }).catch(err => {
          this.$message.error({message: "Error", duration: 2000});
        })
      },

      /**
       * clear event form on cancel
       */
      clearForm() {
        this.eventForm.title = '',
          this.eventForm.location = '',
          this.eventForm.description = '',
          this.dialogFormVisible = false
      },
      // clear pop-over table
      renew() {
        this.gridData = [];
      },
      /**
       * checkbox checked, change buttonDisabled to false(button enabled)
       */
      shareAllSelected(val) {
        this.shareAllChecked = val;
        this.buttonDisable = !val;
      },
      /**
       * share one event using the same API with method shareAll()
       */
      shareOneEvent() {
        this.dialogVisible = false;
        let receiver = this.receiver;
        if (receiver == null || receiver == "" || receiver === "") {
          this.$message.error({message: "Receiver cannot be empty", duration: 2000});
          return;
        }
        let oneEvent = this.editingEvent;
        let list = [];
        list.push({
          id: oneEvent.id, title: oneEvent.title, start: oneEvent.start, end: oneEvent.end,
          location: oneEvent.location, description: oneEvent.description, accountId: oneEvent.accountId
        });
        let params = {
          receiver: receiver,
          events: list
        }
        axios.post(api.ShareEvent, JSON.stringify(params), {
          headers: {
            'content-type': 'application/json'
          }
        })
          .then(res => {
            if (res.data.code != 200) {
              this.$message.error({message: "No Such User", duration: 2000});
              return;
            }
            let shared = res.data.result;
            this.sharedEvents = [];
            this.receiver = "";
            for (let i = 0; i < shared.length; i++) {
              this.sharedEvents.push(shared[i]);
            }
            this.$message.success({message: "Success: Share event to " + receiver, duration: 2000})
          })
      },
      /**
       * share all current user events to dest username or email;
       */
      shareAll() {
        let receiver = this.receiver;
        if (receiver == null || receiver == "" || receiver === "") {
          this.$message.error({message: "Receiver cannot be empty", duration: 2000});
          return;
        }
        this.shareAllButtonVisible = false;  // hide pop-over
        this.shareAllSelected(false); // uncheck all events

        // construct list
        let eventList = this.calendarEvents;
        let list = [];
        for (let i = 0; i < eventList.length; i++) {
          let item = eventList[i];
          let e = {
            id: item.id, title: item.title, start: item.start, end: item.end,
            location: item.location, description: item.description, accountId: item.accountId
          };
          list.push(e);
        }

        let params = {
          receiver: receiver,
          events: list
        }

        axios.post(api.ShareEvent, JSON.stringify(params), {
          headers: {
            'content-type': 'application/json'
          }
        })
          .then(res => {
            if (res.data.code != 200) {
              this.$message.error({message: "No Such User", duration: 2000});
              return;
            }
            let shared = res.data.result;
            this.sharedEvents = [];
            for (let i = 0; i < shared.length; i++) {
              this.sharedEvents.push(shared[i]);
            }
            this.$message.success({message: "Success: Share all events to " + receiver, duration: 2000})
          })
      },

      showReceivedTable() {
        this.hideReceivedTable = !this.hideReceivedTable;

        window.addEventListener('click', () => {
          // if table is being shown, hide it
          if (!this.hideReceivedTable) {
            this.hideReceivedTable = !this.hideReceivedTable;
          }
        })
      },

      /**
       * user log out
       */
      logout() {
        this.calendarEvents = [];
        this.sharedEvents = [];  // clear cached shared events
        this.receivedEvents = [];
        //clear token from localStorage
        console.log("before post-logout");
        console.log(this.$store.token);
        console.log(this.$store.events);
        localStorage.clear();
        this.$store.commit('del_token')
        this.$store.commit('delEvents')
        console.log("after post-logout");
        console.log(this.$store.token);
        console.log(this.$store.events);

        axios
          .get(api.Logout)
          .then(res => {
            this.$router.push('/');
          })
      }
    }
  }

</script>


<style>
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

  .receivedTableHide {
    position: fixed;
    align-self: center;
    text-align: center;
    z-index: 999;
    width: 100%;
    display: none;
  }

  .receivedTableShow {
    position: fixed;
    align-self: center;
    text-align: center;
    z-index: 999;
    width: 100%;
  }


</style>
