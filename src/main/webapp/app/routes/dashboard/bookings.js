import Ember from 'ember';
import ENV from '../../config/environment';

export default Ember.Route.extend({
    currentUser:Ember.inject.service(),
    model() {
        let userid=this.get('currentUser').userId;
        return Ember.$.getJSON({
            url: ENV.APP.API_URL + '/bookings/?getdetails&userid='+userid,
            type: 'GET',
            dataType: 'json'
        })
    }


});
