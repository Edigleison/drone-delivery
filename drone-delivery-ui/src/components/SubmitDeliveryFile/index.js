import React, {useState} from 'react'

import {ApiService} from "../../services/ApiService";
import styles from './styles.module.css'

const SubmitDeliveryFile = () => {
    const [file, setFile] = useState(null);
    const [error, setError] = useState(null);

    const handleFileChange = (event) => {
        setFile(event.target.files[0]);
        setError(null);
    }

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await ApiService.submitInputFile(file, (p) => console.log(p));
            saveApiResponseAsFile(response.data);
        } catch (error) {
            const json = await error.response.data.text();
            const response = JSON.parse(json);
            setError(response.message);
        }
    }

    const saveApiResponseAsFile = (data) => {
        const url = window.URL.createObjectURL(new Blob([data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'output.txt');
        document.body.appendChild(link);
        link.click();

        document.body.removeChild(link);
        URL.revokeObjectURL(url);
    }

    return (
        <div>
            <div className={styles.titleContainer}>
                <h1>Drone Delivery Service</h1>
            </div>
            {error && <div className="alert alert-danger" role="alert">{error}</div>}
            <div className="card">
                <div className="card-body">
                    <form className="row g-3" onSubmit={handleSubmit}>
                        <div className="col-md-6">
                            <label
                                htmlFor="validationCustom03"
                                className="form-label"
                            >Please select the input file</label>
                            <input
                                type="file"
                                className="form-control"
                                id="fileInput"
                                onChange={handleFileChange}
                                required
                            />
                        </div>
                        <div className="col-md-12">
                            <button type="submit" className="btn btn-primary">Submit</button>
                        </div>
                        <div className="col-md-12">
                            The output file will be downloaded after submit the input file
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default SubmitDeliveryFile;
