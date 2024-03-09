import Ember from 'ember';

export default Ember.Service.extend({

  init() {
    /*this.set('isAuthenticated', !!localStorage.getItem('isAuthenticated'));
    this.set('role', (JSON.parse(localStorage.getItem('userData'))).role);
    this.set('userId',(JSON.parse(localStorage.getItem('userData'))).userid);
    this.set('theatreId',(JSON.parse(localStorage.getItem('theatredata'))).theatreid);*/
    if(Ember.isEmpty(this.get('userdata')) && !Ember.isEmpty(localStorage.getItem('userData'))) {
      this.setUserData(JSON.parse(localStorage.getItem('userData')));
    }
  },

  setUserData(userData) {  
    this.set('userdata', userData);
    this.set('isAuthenticated', true);
    this.set('role', userData.role);
    this.set('userId', userData.userid);
    this.set('theatreId',(JSON.parse(localStorage.getItem('theatredata'))).theatreid);
    localStorage.setItem('isAuthenticated', true);
  },

  setTheatreData(tdata){
    localStorage.setItem('theatredata', JSON.stringify(tdata));
    this.set('theatreId',(JSON.parse(localStorage.getItem('theatredata'))).theatreid)
  },

  logout() {
    this.set('isAuthenticated', false);
    this.set('role', null);
    this.set('userId',null);
    localStorage.removeItem('userData')
    localStorage.removeItem('isAuthenticated');
    localStorage.removeItem('theatreId');

  },

});