import Ember from 'ember';
import ENV from '../config/environment';

export default Ember.Route.extend({
    
    currentUser: Ember.inject.service(),

    isAuthenticate: Ember.computed('currentUser.isAuthenticated', function () {
      return this.get('currentUser').get('isAuthenticated');
    }),
    
    isManager: Ember.computed('currentUser.role', function () {
        return this.get('currentUser').get('role') === 2;
    }),

    isAdmin: Ember.computed('currentUser.role', function () {
        return this.get('currentUser').get('role') == 3;
    }),
    
    model(params) {
        let movieID=params.movie_id
        return Ember.$.getJSON(ENV.APP.API_URL+'/movies?listalltheatres&movieid='+movieID).then(theatres => {
            return theatres.filterBy('showstatus', true);
          });
    },
});
