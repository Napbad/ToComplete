import { createApp } from 'vue'
import { createPinia } from 'pinia'
import './style.css'
import App from './App.vue'
import router from './router'

import PrimeVue from 'primevue/config'
import Dialog from 'primevue/dialog'
import Button from 'primevue/button'
import Calendar from 'primevue/calendar'
import Card from 'primevue/card'
import FileUpload from 'primevue/fileupload'
import InputText from 'primevue/inputtext'
import Textarea from 'primevue/textarea'

// Import PrimeVue styles
import 'primevue/resources/themes/lara-light-blue/theme.css'     // theme
import 'primevue/resources/primevue.min.css'                   // core css
import 'primeicons/primeicons.css'                         // icons
import 'primeflex/primeflex.css'                          // flexbox & grid

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)
app.use(PrimeVue, {
  ripple: true,
  inputStyle: "filled"
})

// Register PrimeVue components globally
app.component('Dialog', Dialog)
app.component('Button', Button)
app.component('Calendar', Calendar)
app.component('Card', Card)
app.component('FileUpload', FileUpload)
app.component('InputText', InputText)
app.component('Textarea', Textarea)

app.mount('#app')
