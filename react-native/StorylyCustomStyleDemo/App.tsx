/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import React from 'react';
import {
  Image,
  PixelRatio,
  Platform,
  StatusBar,
  Text,
  useColorScheme,
  View,
} from 'react-native';
import { Colors } from 'react-native/Libraries/NewAppScreen';
import { Storyly } from 'storyly-react-native';


function App(): React.JSX.Element {
  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  return (
    <View style={backgroundStyle}>
      <View style={{paddingTop: 100, height: 170}}>
        <Storyly
        style={{height: 170}}
          storylyId="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NfaWQiOjU1NiwiYXBwX2lkIjoxMzg5LCJpbnNfaWQiOjE4NjY1fQ._PwkZ48JdHkSU01KUR2n66zJcL29JhykNTMRUorfvE4"
          storyGroupViewFactory={{
            customView: CustomPortraitView,
            height: convertToNative(140),
            width: convertToNative(170)
          }}
        />
      </View>
    </View >
  );
}

const CustomPortraitView = ({ storyGroup }) => {
  console.log(`CustomPortraitView: test: ${storyGroup?.title}`)
  return (
    <>
      {storyGroup != null ? (
        <View style={{ width: 100, height: 140 }}>
          <Image source={{ uri: storyGroup?.iconUrl }} style={{ width: 100, height: 100 }} />
          <Text>{storyGroup?.title ?? ""}</Text>
        </View>
      ) : (
        <View />
      )}
    </>
  );
};

const convertToNative = (size: number) => {
  return Platform.OS === 'android' ? PixelRatio.getPixelSizeForLayoutSize(size) : size
}


export default App;
