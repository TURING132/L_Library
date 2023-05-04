import { createRouter, createWebHistory } from 'vue-router';
import Administractor from '../views/Administractor.vue';
import Customer from '../views/Customer.vue';
import Guide from "../views/Guide.vue";
import Register from "../views/Register.vue";
const routes = [
    {
        path: '/',
        name: 'Guide',
        component: Guide,
    },
    {
        path: '/Admin',
        name: 'Admin',
        component: Administractor
    },
    {
        path: '/Customer/:cardId    ',
        name: 'Customer',
        component: Customer,
        props: true
    },
    {
        path: '/Register',
        name: 'Register',
        component: Register
    }
];
const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;
