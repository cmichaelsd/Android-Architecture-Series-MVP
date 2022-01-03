# MVP DEMO - Android Architecture Series Part 2

### Overview
MVP is an architecture made up of a Model, View, and Presenter.
Similar to MVC however the view and controller logic which was previously coexisting in the Activities is now broken out into a Presenter class to separate logic from the View initialization.
- The Presenter should not contain Android framework specific classes to maintain testability.
- A drawback is still the minimal separation of concerns.

### Technologies Used
- RxJava
- Mockito
- Room
- Retrofit
- Glide

### Testing
The contracts for each Contract class are mocked through Mockito allowing testing of each Presenter.
