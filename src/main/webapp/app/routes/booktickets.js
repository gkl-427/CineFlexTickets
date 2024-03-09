import Ember from 'ember';
import ENV from '../config/environment';

export default Ember.Route.extend({

  currentUser: Ember.inject.service(),
  response: [],
  chunk: [],
  isAuthenticate: Ember.computed('currentUser.isAuthenticated', function () {
    return this.get('currentUser').get('isAuthenticated');
  }),

  model(params) {
    let showid = params.showid
    this.controllerFor("booktickets").set("show_id", showid);
    let theatreid = params.theatreid
    return Ember.$.getJSON(ENV.APP.API_URL + '/theatres/' + theatreid + '/shows/' + showid + '?listallseats');
  },

  afterModel: function (model, transition) {
    let response = model;
    let chunk = [];
    for (let i = 0; i < 60; i += 10) {
      chunk.push(response.slice(i, i + 10));
    }
    this.set('seatsModel',chunk);
  },

  setupController(controller) {
    controller.set('model', this.get('seatsModel'));
  },

  actions:{
      willTransition() {
        window.location.reload(true);
      }
  }
});
