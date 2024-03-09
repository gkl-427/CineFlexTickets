import Ember from 'ember';

export default Ember.Controller.extend({
    currentUser: Ember.inject.service(),
    isAdmin: Ember.computed('currentUser.role', function () {
        return this.get('currentUser').get('role') == 3;
    }),

    filteredData: null,
    isTheatreAvailable:true,
    actions:
    {
        searchCustomers() {
            let searchQuery = this.get("searchQuery");
            let filteredData = this.get("model").filter((model) => {
                return model.theatrename.toLowerCase().includes(searchQuery.toLowerCase());
            });
            this.set("filteredData", filteredData);
            if (filteredData == null && searchQuery != null) {
                this.set("isTheatreAvailable", true);
              } else if (filteredData != null) {
                this.set("isTheatreAvailable", false);
              }
        }
    }
});
