import Ember from 'ember';
import ENV from '../../config/environment';


export default Ember.Route.extend({
    currentUser:Ember.inject.service(),
    model() {
            let id=this.get('currentUser').get('userId');
            return Ember.$.getJSON({
                url: ENV.APP.API_URL + '/theatres?userid='+id,
                type: 'GET',
                dataType: 'json'
            }).then(response => {
                let theatredetails=response.filter(item => item.userid == id);
                if(theatredetails.length>0) this.get('currentUser').setTheatreData(theatredetails[0]);
                return theatredetails
            })
        } 
});
