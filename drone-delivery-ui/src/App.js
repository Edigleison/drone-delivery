import "bootstrap/dist/css/bootstrap.min.css";
import styles from "./App.module.css"
import SubmitDeliveryFile from "./components/SubmitDeliveryFile";

function App() {
    return (
        <div className={["container-fluid", styles.app].join(" ")}>
            <SubmitDeliveryFile/>
        </div>
    );
}

export default App;
