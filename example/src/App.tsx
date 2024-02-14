import * as React from 'react';

import { StyleSheet, View, Text } from 'react-native';
import { selectDocument } from '@sephriot/react-native-persistable-uri';

export default function App() {
  const [result, setResult] = React.useState<string | undefined>();

  return (
    <View style={styles.container}>
      <Text
        style={{ marginBottom: 20 }}
        onPress={() => selectDocument('audio/*').then(setResult)}
      >
        SELECT!
      </Text>
      <Text>Result: {result}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
