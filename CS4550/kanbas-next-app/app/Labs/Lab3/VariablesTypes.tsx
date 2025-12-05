export default function VariableTypes() {
  let numberVariable = 123;
  let floatingPointNumber = 234.345;
  let booleanVariable = true;
  let stringVariable = 'Hello World!';
  
  let isNumber = typeof numberVariable;
  let isString = typeof stringVariable;
  let isBoolean = typeof booleanVariable;

  return (
    <div id="wd-variable-types">
      <h4>Variable Types</h4>
      numberVariable = {numberVariable} <br />
      floatingPointNumber = {floatingPointNumber} <br />
      stringVariable = {stringVariable} <br />
      booleanVariable = {booleanVariable.toString()} <br />
      isNumber = {isNumber} <br />
      isString = {isString} <br />
      isBoolean = {isBoolean} <br />
    </div>
  );
}
