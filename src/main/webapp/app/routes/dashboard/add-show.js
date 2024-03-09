import Ember from 'ember';
import ENV from '../../config/environment';

export default Ember.Route.extend({

    model(){
        return Ember.$.getJSON({
            url: ENV.APP.API_URL + '/movies',
            type: 'GET',
            dataType: 'json'
        })
    }
});
