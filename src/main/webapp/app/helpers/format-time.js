import Ember from 'ember';

export function formatTime([time]) {
  if (time) {
    let date = new Date(time);
    return date.toLocaleTimeString(); 
  }
}

export default Ember.Helper.helper(formatTime);
