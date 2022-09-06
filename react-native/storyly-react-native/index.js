import { NativeModules, findNodeHandle } from 'react-native';

const LINKING_ERROR =
  `The package 'storyly-moments-react-native' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const StorylyMonetization = NativeModules.STStorylyMonetization
  ? NativeModules.STStorylyMonetization
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

const setAdViewProvider = (ref, param) => {
    let id = findNodeHandle(ref._storylyView);
    console.log(id);
    StorylyMonetization.setAdViewProvider(findNodeHandle(ref._storylyView), param);
};

module.exports = {
    setAdViewProvider,
    get Storyly() {
        return require('./RNStoryly').default;
    },
}
