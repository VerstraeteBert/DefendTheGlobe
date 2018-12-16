<template>
    <div class="register">
        <md-card class="md-primary">
            <md-card-header>
                <div class="md-title">Register</div>
            </md-card-header>
            <md-card-content>
              <div v-if="message.message === 'failure'">
                <ul>
                 <li class="errormsg" v-for="error in message.errors">
                   {{error}}
                 </li>
                </ul>
              </div>

              <div class="errormsg" v-if="message.message == 'duplicate'">Username or email already in use</div>
                <md-input-container>

                    <md-icon>person</md-icon>
                  <label>Username</label>
                    <md-input v-model="name" type="text" name="name" required></md-input>
                </md-input-container>
                <md-input-container>
                    <md-icon>email</md-icon>
                    <label>E-mail</label>
                    <md-input v-model="email" type="text" name="email" required></md-input>
                </md-input-container>
                <md-input-container md-has-password>
                    <md-icon>lock</md-icon>
                    <label>Password</label>
                    <md-input v-model="password" type="password" name="password" required></md-input>
                </md-input-container>
            </md-card-content>
            <md-card-actions>
                <md-button v-on:click="giveBackInfoToParent" class="md-raised md-about">Register!</md-button>
                <md-button v-on:click="swapToLoginForm" class="md-raised md-about">Login</md-button>
            </md-card-actions>
        </md-card>
    </div>
</template>

<script>
    export default {
        name: "Register",
        data() {
            return {
                email: 'test123@gmail.com',
                name: 'test1',
                password: 'Test?123G'
            }

        },
        methods: {
            giveBackInfoToParent() {
                this.$emit('userRegister', this.email, this.name, this.password);
            },
            swapToLoginForm() {
                this.$emit('loginForm', 'login');
            }
        },
        sockets: {},
        props: ['message']
    }
</script>

<style>
    .register {
        z-index: 100;
        width: 300px;
    }

  ul{
    list-style-type: none;
  }

</style>
