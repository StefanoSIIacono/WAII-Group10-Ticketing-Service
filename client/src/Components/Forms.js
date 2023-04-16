import { useState } from "react";
import { Button, Form } from 'react-bootstrap';
import { Profile } from "./Profile";
import { useNavigate, NavLink } from "react-router-dom";

function GetProfileForm(props) {
    const [email, setEmail] = useState('');

    const handleSubmit = (event) => {
        event.preventDefault();
        props.readProfileByMail(email);
        props.setMode('edit');
      }

    return <>
        <Form onSubmit={handleSubmit}>
            <Form.Group >
                <Form.Control type='text' value={email} required={true} placeholder="Email" onChange={(event) => { setEmail(event.target.value) }} />
            </Form.Group>
            <div align='right'>
                <Button type='submit' variant='dark'>Get</Button>
            </div>
        </Form>
    </>
}

function EditProfileForm(props) {
    var navigate = useNavigate();
    const [name, setName] = useState(props.profile.name);
    const [surname, setSurname] = useState(props.profile.surname);


    const handleSubmit = (event) => {
        event.preventDefault();
        props.editProfile(new Profile(props.profile.email, name, surname));
        props.setMode('show');
        navigate('/profiles');
    }

    return <>
        <div style={{ padding: 10 }}>
            <Form onSubmit={handleSubmit}>
                <Form.Group >
                    <Form.Label>Name</Form.Label>
                    <Form.Control type='text' value={name} required={true} placeholder="Name" onChange={(event) => { setName(event.target.value) }} />
                </Form.Group>
                <Form.Group >
                    <Form.Label>Surname</Form.Label>
                    <Form.Control type='text' value={surname} required={true} placeholder="Surname" onChange={(event) => { setSurname(event.target.value) }} />
                </Form.Group>
                <div align='right'>
                    <NavLink to='/profiles'><Button variant='secondary' onClick={() => props.setMode('show')}>Cancel</Button></NavLink> &nbsp;
                    <Button type='submit' variant='success'>Save</Button>
                </div>
            </Form>
        </div>
    </>

}


function AddProfileForm(props) {
    var navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [name, setName] = useState('');
    const [surname, setSurname] = useState('');

    const handleSubmit = (event) => {
        event.preventDefault();
        props.addProfile(new Profile(email, name, surname));

        setEmail('');
        setName('');
        setSurname('');
        navigate('/profiles');

    }

    return <>
        <div style={{ padding: 10 }}>
            <Form onSubmit={handleSubmit}>
                <Form.Group >
                    <Form.Label>Email</Form.Label>
                    <Form.Control type='text' value={email} required={true} placeholder="Email" onChange={(event) => { setEmail(event.target.value) }} />
                </Form.Group>
                <Form.Group >
                    <Form.Label>Name</Form.Label>
                    <Form.Control type='text' value={name} required={true} placeholder="Name" onChange={(event) => { setName(event.target.value) }} />
                </Form.Group>
                <Form.Group >
                    <Form.Label>Surname</Form.Label>
                    <Form.Control type='text' value={surname} required={true} placeholder="Surame" onChange={(event) => { setSurname(event.target.value) }} />
                </Form.Group>
                <div align='right'>
                    <NavLink to='/profiles'><Button variant='secondary'>Cancel</Button></NavLink> &nbsp;
                    <Button type='submit' variant='success'>Add</Button>
                </div>
            </Form></div>
    </>

}

export { AddProfileForm, EditProfileForm, GetProfileForm};