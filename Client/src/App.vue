<template>
    <div id="app">
        <router-view></router-view>
    </div>
</template>

<script>
    let self;

    export default {
        name: 'app',
        data() {
            return {}
        },
        created() {
            self = this;
        }
    }
</script>

<style>
    body, html {
        height: 100%;
    }

    #app {
        height: 100%;
        /*background-color: ;*/
    }

    header, form {
        z-index: 500;
    }

    header {
        color: white;
    }
</style>
