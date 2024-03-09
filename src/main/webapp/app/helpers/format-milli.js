import Ember from 'ember';

export function formatTime([time]) {
  if (time) {
    const hours=(time/60*60*1000);
    time = time - hours*(60*60*1000);
    const min=time/(60*1000);
    return hours+':'+min;
  }
}

export default Ember.Helper.helper(formatTime);