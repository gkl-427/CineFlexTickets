import Ember from 'ember';
import ENV from '../../config/environment';

export default Ember.Controller.extend({
    currentUser: Ember.inject.service(),

    isManager: Ember.computed('currentUser.role', function () {
        return this.get('currentUser').get('role') == 2;
    }),

    theatreid:null,
    shownumber: null,
    showdate: null,
    showtime:null,
    timevalue:null,
    milli:null,
    availableshows: [
        { Value: '' },
        { Value: 1 },
        { Value: 2 },
        { Value: 3 },
    ],
    today:new Date().toISOString().slice(0, 10),
    timeStringToMilliseconds(timeString) {
        const [hours, minutes] = timeString.split(':').map(Number);
        if(hours>12){
            const totalMilliseconds = (hours * 60 + minutes) * 60 * 1000;
            return totalMilliseconds-(3600000+14400000+1800000);
        }else{
            const totalMilliseconds = (hours * 60 + minutes) * 60 * 1000;
            return totalMilliseconds-(10800000+1800000);
        }
      },
    actions: {

        selectmovie(Value) {
            this.set('movieid', Value);
        },
        updateDate(date) {
            this.set('currentDate', date);
        },
        selectshownumber(Value) {
            this.set('shownumber', Value);
        },
        updateTime(timevalue){
            this.set('showtime',timevalue);
        },
        sendRecord() {
            let formData = {
                shownumber: this.get('shownumber'),
                showdate: this.get('currentDate'),
                movieid:this.get('movieid'),
                showstatus:'true',
                theatreid:this.get('currentUser.theatreId'),
                showstarttime:this.timeStringToMilliseconds(this.showtime)

            };
            Ember.$.ajax({
                url: ENV.APP.API_URL + '/shows',
                type: 'POST',
                data: JSON.stringify(formData),
                dataType: 'application/json'
            }).then(()=> {
                let $element = Ember.$('.element-to-animate');
                $element.addClass('fade-out');
          
                Ember.run.later(() => {
                  this.transitionTo('dashboard.shows');
                }, 500); 
            }).catch(()=>{
                let $element = Ember.$('.element-to-animate');
                $element.addClass('fade-out');
          
                Ember.run.later(() => {
                  this.transitionToRoute('dashboard.shows');
                }, 500); 
            })
        }
        },
        
});
