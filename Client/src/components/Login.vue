<template>
    <div class="login">
        <md-card class="md-primary">
            <md-card-header>
                <div class="md-title">Login</div>
            </md-card-header>
            <md-card-content>
              <div class="errormsg" v-if="message.message === 'failure'">
                Username of passwoord incorrect ingevuld
              </div>
                <md-input-container>
                    <md-icon>person</md-icon>
                    <label>Username</label>
                    <md-input v-model="name" type="text" name="name" required></md-input>
                </md-input-container>
                <md-input-container md-has-password>
                    <md-icon>lock</md-icon>
                    <label>Password</label>
                    <md-input v-model="password" type="password" name="password" required></md-input>
                </md-input-container>
            </md-card-content>
            <md-card-actions>
                <md-button @keyup.enter="giveBackInfoToParent" v-on:click="giveBackInfoToParent" class="md-raised md-about">Login!</md-button>
                <md-button v-on:click="swapToRegister" class="md-raised md-about">Register</md-button>
            </md-card-actions>
        </md-card>

    </div>
</template>

<script>
    export default {
        name: "Login",
        data() {
            return {
                name: 'Bert123',
                password: ''
            }
        },
        props: ['message'],
        methods: {
            giveBackInfoToParent() {
                this.$emit('userLogin', this.name, this.password);
            },
            swapToRegister() {
                this.$emit('registerForm', 'register');
            },
            submit(e) {
              console.log(e);
              if (e.keyCode === 13) this.giveBackInfoToParent();
            }
        },
        created() {
          window.addEventListener('keydown', this.submit);
        },
        beforeDestroy() {
          window.removeEventListener('keydown', this.submit);
        }
    }
</script>

<style scoped>
    .login {
        z-index: 100;
        width: 300px;
    }


</style>
