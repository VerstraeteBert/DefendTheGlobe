<template>
  <div id="Home">
    <!-- <header>
      <h1>Welcome to Atari-Breakout</h1>
      <h3>Group 17</h3>
    </header> -->
    <md-layout mdAlign="center" mdVerticalAlign="center" mdGutter="mdGutter">
      <md-layout>
        <StarField></StarField>

        <Login v-if="form === 'login'" @userLogin="loginToServer" :message="message" @registerForm="swapForm"></Login>
        <Register v-else @userRegister="registerToServer" :message="message" @loginForm="swapForm"></Register>
      </md-layout>
    </md-layout>
    <div></div>
  </div>
</template>

<script>

  import Register from './Register';
  import Login from './Login';
  import StarField from './StarField';
  import Vue from "vue";


  let self;
  export default {
    name: 'Home',
    data() {
      return {
        form: 'login',
        message: ''
      }
    },
    components: {
      Register,
      Login,
      StarField,

    },
    methods: {
      loginToServer(name, password) {
        const options = {
          method: "POST",
          headers: {'Content-Type': 'application/x-www-form-urlencoded'},
          body: `name=${name}&password=${password}`
        };
        fetch("/AtariBreakout/login", options)
          .then(res => res.text())
          .then(res => {
            if (res.status === "ok") {
              this.$session.set("username", data.message);
              this.$router.push("/Lobby");
              this.$socket.send(JSON.stringify({
                action: "setUserWithSession",
                name: data.message
              }));

            } else {
              this.$session.set("username", "illegal user");
              this.$toasted.error("illegal user but idc");
              this.$router.push("/Lobby");
            }

          })
          .catch(error => {
            console.log("Error fetching " + error);
          })
      },
      registerToServer(email, name, password) {
        const registerObj = {email, name, password};
        console.log("user trying to register ", JSON.stringify(registerObj));
        const options = {
          method: "POST",
          headers: {'Content-Type': 'application/x-www-form-urlencoded'},
          body: `email=${email}&name=${name}&password=${password}`
        };

        fetch("/AtariBreakout/register", options)
          .then((resp) => resp.json())
          .then(function (data) {
              console.log(data.message);
              console.log(data);
              self.checkRegisterMessage(data);
            }
          )
          //IF MESSAGE: SUCCES => correct ingelogd, duplicate => email of gebruikersnaam bestaat al, failure => form niet valid
          .catch(error => {
            console.log("Error fetching ", error);
            self.message.errors = [error];
            self.showErrors();
          })
      },
      checkRegisterMessage(data) {
        if (data.message === "success") {
          console.log("success");
          this.$session.set("username", data.message);
          this.$router.push("/Lobby");
        }
        if (data.message === "duplicate" || data.message === "failure") {
          console.log("incorrect");
          this.message = data;
          this.showErrors();
        }
      },
      swapForm(action) {
        this.form = action;
      },
      showErrors() {
        this.message.errors.forEach(function (value, index) {
          console.log("showing errors");
          self.$toasted.error(value);
        })
      }
    },
    created() {
      self = this;
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
  #Home {
    display: flex;
    justify-content: space-around;
    align-items: center;
    flex-direction: column;
    height: 100%;
  }

  header {
    text-align: center;
  }

  input:nth-child(even) {
    margin-top: 5px;
    margin-bottom: 5px;
  }

  .errormsg {
    background-color: deeppink;
  }
  .md-input {
    margin: 0;
  }
</style>
